package com.honnix.cheater.validator

import org.apache.commons.httpclient.{Header, HttpClient, HttpMethodBase}

object CookieValidator {
  private val DSId = "DSID"

  private val DSFirstAccess = "DSFirstAccess"

  private val DSLastAccess = "DSLastAccess"
}

class CookieValidator extends Validator {
  override def isLoginValid(httpClient: HttpClient, method: HttpMethodBase): Boolean = {
    val cookies = httpClient.getState.getCookies

    cookies.exists(CookieValidator.DSId == _.getName) &&
    cookies.exists(CookieValidator.DSFirstAccess == _.getName) &&
    cookies.exists(CookieValidator.DSLastAccess == _.getName)
  }

  override def isLogoutValid(httpClient: HttpClient, method: HttpMethodBase): Boolean = true

  override def isHeartbeatValid(httpClient: HttpClient, method: HttpMethodBase): Boolean = true
}
