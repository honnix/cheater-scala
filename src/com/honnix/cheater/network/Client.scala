package com.honnix.cheater.network

trait Client {
  def login: Boolean
  def heartbeat: Boolean
  def logout: Boolean
}
