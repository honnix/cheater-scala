package com.honnix.cheater.exception

object SpcException {
  def apply(message: String, cause: Throwable): Exception = 
    (new SpcException1(message, cause)).asInstanceOf[Exception]

  def apply(message: String): Exception = 
    (new SpcException2(message)).asInstanceOf[Exception]

  def apply(cause: Throwable): Exception = 
    (new SpcException3(cause)).asInstanceOf[Exception]

  def apply(): Exception = 
    (new SpcException4).asInstanceOf[Exception]
}

trait SpcException

class SpcException1(message: String, cause: Throwable)
    extends Exception(message, cause) with SpcException

class SpcException2(message: String)
    extends Exception(message) with SpcException

class SpcException3(cause: Throwable)
    extends Exception(cause) with SpcException

class SpcException4 extends Exception with SpcException
