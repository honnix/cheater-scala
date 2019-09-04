package com.honnix.cheater.network

class FakeClient extends Client {
  def login: Boolean = {println("Logging in..."); true}

  def logout: Boolean = {println("Logging out..."); true}

  def heartbeat: Boolean = {println("Heartbeating..."); true}
}
