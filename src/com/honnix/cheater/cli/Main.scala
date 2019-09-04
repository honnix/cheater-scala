package com.honnix.cheater.cli

import java.net.URISyntaxException
import java.net.URL

import org.apache.commons.logging.{Log, LogFactory}

import cheater.service.CheaterImpl
import cheater.constant.CheaterConstant

object Main {
  private lazy val Log = LogFactory.getLog(getClass)
  
  private def setTrustStore(): Boolean = {
    val trustStoreFileURL = getClass.getClassLoader.getResource(CheaterConstant.TrustStoreFileName)

    if (null ne trustStoreFileURL) {
      val trustStoreFilePath = try {
        trustStoreFileURL.toURI.getPath
      } catch {
        case e: URISyntaxException =>
          Log.fatal("Failed setting trust store", e)
          null
      }

      if (null ne trustStoreFilePath) {
        System.setProperty(CheaterConstant.TrustStoreKey, trustStoreFilePath)
        true
      } else false
    } else false
  }

  def main(args: Array[String]) {
    if ((null eq System.getProperty(CheaterConstant.TrustStoreKey)) &&
        !setTrustStore()) {
      Log.fatal("Failed setting trust store. Use cheater.sh instead.")
      exit(1)
    }

    CheaterImpl().startService
  }
}
