package com.honnix.cheater.admin

import java.io.{IOException, PrintWriter}
import java.net.{ServerSocket, Socket, SocketException}

import scala.actors.{Actor, TIMEOUT}
import scala.actors.Actor._

import org.apache.commons.logging.{Log, LogFactory}

import cheater.service.Cheater

object AdminServer {
  private lazy val Log = LogFactory.getLog(getClass)
  
  def apply(serverSocket: ServerSocket, cheater: Cheater) =
    new AdminServer(serverSocket, cheater)
}

class AdminServer(serverSocket: ServerSocket,
                  cheater: Cheater) extends Actor {
  import AdminServer.Log
  
  private def work(client: Socket) {
    var clientSocket: Socket = try {
      serverSocket.accept
    } catch {
      case e: SocketException => null
      case e: IOException =>
        Log.error("Failed accepting admin socket connection",
                  e)
        work(client)
        null
    }

    if (null ne clientSocket) {
      if ((null ne client) && !client.isClosed) {
        try {
          val pw = new PrintWriter(clientSocket.getOutputStream)
          pw.println("Someone else is administrating me.")
          pw.flush
          clientSocket.close
        } catch {
          case e: IOException =>
            Log.warn("Error operating client socket.",
                     e)
        }
        work(client)
      } else {
        AdminClient(this, clientSocket).start
        work(clientSocket)
      }
    }
  }

  def getCheater = cheater

  def stop = serverSocket.close

  def act {work(null)}
}
