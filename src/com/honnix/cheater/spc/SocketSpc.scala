package com.honnix.cheater.spc

import java.io.IOException
import java.net.ServerSocket

import cheater.service.Cheater
import cheater.admin.AdminServer
import cheater.constant.CheaterConstant

class SocketSpc extends Spc {
  def check(cheater: Cheater): Boolean = {
    try {
      AdminServer(new ServerSocket(
        CheaterConstant.SocketSpcPort), cheater).start
      true
    } catch {
      case e: IOException => false
    }
  }
}
