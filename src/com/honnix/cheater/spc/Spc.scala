package com.honnix.cheater.spc

import cheater.service.Cheater

trait Spc {
  def check(cheater: Cheater): Boolean
}
