package com.honnix.cheater.spc

import cheater.service.Cheater

class FakeSpc extends Spc {
  def check(cheater: Cheater): Boolean = true
}
