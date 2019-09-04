package com.honnix.cheater.validator

import org.apache.commons.httpclient.{HttpClient, HttpMethodBase}

trait Validator {
  def isLoginValid(httpClient: HttpClient, method: HttpMethodBase): Boolean = true

  def isLogoutValid(httpClient: HttpClient, method: HttpMethodBase): Boolean = true

  def isHeartbeatValid(httpClient: HttpClient, method: HttpMethodBase): Boolean = true
}
