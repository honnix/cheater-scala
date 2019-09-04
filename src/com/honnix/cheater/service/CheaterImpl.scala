package com.honnix.cheater.service

import scala.actors.{Actor, TIMEOUT}
import scala.actors.Actor._

import org.apache.commons.logging.{Log, LogFactory}

import cheater.constant.CheaterConstant
import cheater.network.Client
import cheater.spc.Spc
import cheater.exception.SpcException

object CheaterImpl {
  private lazy val Log = LogFactory.getLog(getClass)
  
  def apply() = new CheaterImpl
}

class CheaterImpl extends Cheater with Actor {
  import CheaterImpl.Log

  private var currentStatus = CheaterConstant.Initializing

  private val client = {
    try {
      Class.forName(CheaterConstant.Client).newInstance().asInstanceOf[Client]
    } catch {
      case e: Exception =>
        Log.fatal("Could not instantiate client. Assume testing run.", e)
      import cheater.network.FakeClient
      new FakeClient
    }
  }

  private val spc = {
    try {
      Class.forName(CheaterConstant.Spc).newInstance.asInstanceOf[Spc]
    } catch {
      case e: Exception =>
        Log.fatal("Could not instantiate spc. Assume testing run.", e)
      import cheater.spc.FakeSpc
      new FakeSpc
    }
  }

  currentStatus = CheaterConstant.Initialized

  private def canStart: Boolean = currentStatus <= CheaterConstant.Initialized ||
    currentStatus == CheaterConstant.Stopped

  private def canStop: Boolean = currentStatus >= CheaterConstant.Starting &&
    currentStatus < CheaterConstant.Stopping

  private def logout() {
    currentStatus = CheaterConstant.LoggingOut

    if (client.logout)
      Log.info("Logout successfully.")
    else
      Log.warn("Logout failed. But it's OK.")

    currentStatus = CheaterConstant.LoggedOut

    work(0)
  }

  private def heartbeatLoop(livingTime: Int, failedTimes: Int) {
    if (livingTime < 1 * CheaterConstant.HoursToLive) {
      if (client.heartbeat) {
        currentStatus = CheaterConstant.Heartbeating
        reactWithin(CheaterConstant.HeartbeatInterval * 1000) {
          case 'stop => exit
          case TIMEOUT =>
            heartbeatLoop(livingTime + 1, 0)
            logout
        }
      }
      else if (failedTimes + 1 < CheaterConstant.HeartbeatMaxFailTimes) {
        currentStatus = CheaterConstant.HeartbeatingRetrying
        Log.warn("Heartbeat failed. Wait " +
                 CheaterConstant.HeartbeatInterval +
                 " seconds to retry.")

        reactWithin(CheaterConstant.HeartbeatInterval * 1000) {
          case 'stop => exit
          case TIMEOUT =>
            heartbeatLoop(0, failedTimes + 1)
            logout
        }
      } else {
        currentStatus = CheaterConstant.HeartbeatingRetryFailed
        Log.warn("Heartbeat failed continuously for " +
                 CheaterConstant.HeartbeatMaxFailTimes +
                 " times. Try to login again.")
      }
    }
  }

  private def work(retryTimes: Int) {
    if (client.login) {
      currentStatus = CheaterConstant.LoggedIn

      Log.info("Login successfully")

      heartbeatLoop(0, 0)
    } else {
      if (retryTimes < CheaterConstant.LoginMaxRetryTimes) {
        currentStatus = CheaterConstant.LoggingInRetrying

        Log.error("Login failed. Wait " +
                  CheaterConstant.LoginRetryInterval +
                  " seconds to retry.")

        reactWithin(CheaterConstant.LoginRetryInterval * 1000) {
          case 'stop => exit
          case TIMEOUT => work(retryTimes + 1)
        }
      } else {
        Log.error("Maximum login retry times reached. Quit...")

        currentStatus = CheaterConstant.LoggingInRetryFailed
      }
    }
  }

  def startService {
    if (canStart) {
      if (!spc.check(this))
        throw SpcException(CheaterConstant.SpcErrorMessage)
      start
      currentStatus = CheaterConstant.Started
    }
  }

  def stopService {
    if (canStop) {
      this ! 'stop
      currentStatus = CheaterConstant.Stopped
    }
  }

  def getCurrentStatus: Int = currentStatus

  def act = work(0)
}
