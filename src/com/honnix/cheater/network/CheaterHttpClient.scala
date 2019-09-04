package com.honnix.cheater.network

import org.apache.commons.httpclient.{Header, HttpClient, HttpMethod}
import org.apache.commons.httpclient.cookie.CookiePolicy
import org.apache.commons.httpclient.methods.{GetMethod, PostMethod}
import org.apache.commons.httpclient.params.HttpMethodParams

import org.apache.commons.logging.{Log, LogFactory}

import cheater.constant.CheaterConstant
import cheater.validator.{ContentValidator, CookieValidator, Validator}

object CheaterHttpClient {
  private lazy val Log = LogFactory.getLog(getClass)
}

class CheaterHttpClient extends Client {
  import CheaterHttpClient.Log

  private val httpClient = new HttpClient

  httpClient.getParams.setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY)
  httpClient.getParams.setParameter("http.useragent", CheaterConstant.UserAgent)
  httpClient.getParams.setParameter(HttpMethodParams.SINGLE_COOKIE_HEADER,
                                    true)

  private val validatorList = List(new CookieValidator, new ContentValidator)

  private def execute(method: HttpMethod)(validate: => Boolean): Boolean = {
    val result = try {
      httpClient.executeMethod(method)

      val locationHeader = method.getResponseHeader("location")
      if (null ne locationHeader)
        httpClient.executeMethod(new GetMethod(locationHeader.getValue))

      true
    } catch {
      case e: Exception =>
        Log.error("Method execution error.", e)
        false
    }

    result && validate
  }

  def login: Boolean = {
    httpClient.getState.clearCookies

    val postMethod = new PostMethod(CheaterConstant.LoginUrl)
    postMethod.setParameter(CheaterConstant.UserNameKey, CheaterConstant.User)
    postMethod.setParameter(CheaterConstant.PasswordKey, CheaterConstant.Password)
    postMethod.setParameter(CheaterConstant.TimezoneOffsetKey, CheaterConstant.TimezoneOffset)
    postMethod.setParameter(CheaterConstant.RealmKey, CheaterConstant.Realm)
    
    val result = execute(postMethod) {
      validatorList.forall(_.isLoginValid(httpClient, postMethod))
    }
    postMethod.releaseConnection

    result
  }

  def logout: Boolean = {
    val getMethod = new GetMethod(CheaterConstant.LogoutUrl)
    val result = execute(getMethod) {
      validatorList.forall(_.isLogoutValid(httpClient, getMethod))
    }
    getMethod.releaseConnection

    result
  }

  def heartbeat: Boolean = {
    val getMethod = new GetMethod(CheaterConstant.LogoutUrl)
    val result = execute(getMethod) {
      validatorList.forall(_.isHeartbeatValid(httpClient, getMethod))
    }
    getMethod.releaseConnection

    result
  }
}
