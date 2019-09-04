package com.honnix.cheater.service

trait Cheater {
  def startService
  def stopService
  def getCurrentStatus: Int
}
