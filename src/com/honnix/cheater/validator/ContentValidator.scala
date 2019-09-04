package com.honnix.cheater.validator

import java.io.{IOException, BufferedReader, InputStreamReader}

import scala.io.Source

import org.apache.commons.httpclient.{Header, HttpClient, HttpMethodBase}

import cheater.constant.CheaterConstant

object ContentValidator {
  private val HeartbeatKeyString = "function Heartbeat()"

  private val LogoutKeyString = "Click here to sign in again"
}

class ContentValidator extends Validator {
  private def isValidContent(method: HttpMethodBase, keyString: String): Boolean = {
    val charSet = method.getResponseCharSet

    try {
      Source.fromInputStream(method.getResponseBodyAsStream,
                             charSet).getLines.exists(_.indexOf(keyString) != -1)
    } catch {
      case e: IOException => false
    }
  }

  override def isLoginValid(httpClient: HttpClient, method: HttpMethodBase): Boolean = {
    val header = method.getResponseHeader("location")

    if ((null ne header) && CheaterConstant.AfterLoginUrl == header.getValue)
      true
    else false
  }

  override def isLogoutValid(httpClient: HttpClient, method: HttpMethodBase): Boolean =
    isValidContent(method, ContentValidator.HeartbeatKeyString)

  override def isHeartbeatValid(httpClient: HttpClient, method: HttpMethodBase): Boolean =
    isValidContent(method, ContentValidator.HeartbeatKeyString)
}
