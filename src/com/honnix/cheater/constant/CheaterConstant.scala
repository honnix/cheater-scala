package com.honnix.cheater.constant

import java.util.Properties

import scala.actors.Actor._

import cheater.util.PropertiesLoader

object CheaterConstant {
  private val CheaterPropertiesFileName = "cheater.properties"

  val TrustStoreFileName = PropertiesLoader.loadProperties(
    CheaterPropertiesFileName).getProperty("cert.file.name")

  val Client = PropertiesLoader.loadProperties(
    CheaterPropertiesFileName).getProperty("client")

  val Spc = PropertiesLoader.loadProperties(
    CheaterPropertiesFileName).getProperty("spc")

  val SocketSpcPort = PropertiesLoader.loadProperties(
    CheaterPropertiesFileName).getProperty("socket.spc.port").toInt

  val LoginMaxRetryTimes = PropertiesLoader.loadProperties(
    CheaterPropertiesFileName).getProperty("login.max.retry.times").toInt

  val LoginRetryInterval = PropertiesLoader.loadProperties(
    CheaterPropertiesFileName).getProperty("login.retry.interval").toInt

  val HoursToLive = PropertiesLoader.loadProperties(
    CheaterPropertiesFileName).getProperty("hours.to.live").toInt

  val HeartbeatInterval = PropertiesLoader.loadProperties(
    CheaterPropertiesFileName).getProperty("heartbeat.interval").toInt

  val HeartbeatMaxFailTimes = PropertiesLoader.loadProperties(
    CheaterPropertiesFileName).getProperty("heartbeat.max.fail.times").toInt

  val UserAgent = PropertiesLoader.loadProperties(
    CheaterPropertiesFileName).getProperty("user.agent")

  val LoginUrl = PropertiesLoader.loadProperties(
    CheaterPropertiesFileName).getProperty("login.url")

  val LogoutUrl = PropertiesLoader.loadProperties(
    CheaterPropertiesFileName).getProperty("logout.url")

  val HeartbeatUrl = PropertiesLoader.loadProperties(
    CheaterPropertiesFileName).getProperty("heartbeat.url")

  val AfterLoginUrl = PropertiesLoader.loadProperties(
    CheaterPropertiesFileName).getProperty("after.login.url")

  val User = PropertiesLoader.loadProperties(
    CheaterPropertiesFileName).getProperty("user.name")

  val Password = PropertiesLoader.loadProperties(
    CheaterPropertiesFileName).getProperty("password")

  val TimezoneOffset = PropertiesLoader.loadProperties(
    CheaterPropertiesFileName).getProperty("timezone.offset")

  val Realm = PropertiesLoader.loadProperties(
    CheaterPropertiesFileName).getProperty("realm")

  val TrustStoreKey = "javax.net.ssl.trustStore"

  val SpcErrorMessage = 
    "Only one cheater process is allowed. Check with 'ps -ef | grep 'cheater''"

  val UserNameKey = "username"

  val PasswordKey = "password"

  val TimezoneOffsetKey = "tz_offset"

  val RealmKey = "realm"

  val Initializing = 0x0001

  val Initialized = 0x0002

  val Starting = 0x0003

  val Started = 0x0004

  val LoggingIn = 0x0005

  val LoggedIn = 0x0006

  val Heartbeating = 0x0007

  val HeartbeatingRetrying = 0x0008

  val HeartbeatingRetryFailed = 0x0009

  val LoggingOut = 0x000A

  val LoggedOut = 0x000B

  val LoggingInRetrying = 0x000C

  val LoggingInRetryFailed = 0x000D

  val Stopping = 0x000E

  val Stopped = 0x000F

  val StatusMap = Map(Initializing -> "initializing",
                    Initialized -> "initialized",
                    Starting -> "starting",
                    Started -> "started",
                    LoggingIn -> "logging in",
                    LoggedIn -> "logged in",
                    Heartbeating -> "heartbeating",
                    HeartbeatingRetrying -> "heartbeating retrying",
                    HeartbeatingRetryFailed -> "heartbeating retry failed",
                    LoggingOut -> "logging out",
                    LoggedOut -> "logged out",
                    LoggingInRetrying -> "logging in retrying",
                    Stopping -> "stopping",
                    Stopped -> "stopped",
                    LoggingInRetryFailed -> "logging in retry failed")
}
