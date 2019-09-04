package com.honnix.cheater.admin

import java.io.{BufferedReader, IOException, InputStreamReader, PrintWriter}
import java.net.Socket

import scala.actors.Actor

import cheater.constant.CheaterConstant

object AdminClient {
  private val Prompt = "> "
  
  def apply(adminServer: AdminServer, clientSocket: Socket) =
	new AdminClient(adminServer, clientSocket)
}

class AdminClient(adminServer: AdminServer,
                  clientSocket: Socket) extends Actor {
  import AdminClient.Prompt

  private def handleRequest(br: BufferedReader,
                            pw: PrintWriter): Unit = br.readLine match {
    case "status" =>
      pw.println(CheaterConstant.StatusMap(
        adminServer.getCheater.getCurrentStatus))
      pw.print(Prompt)
      pw.flush
      handleRequest(br, pw)
    case "quit" =>
      clientSocket.close
    case "help" =>
      pw.println("status    to show current status of cheater");
      pw.println("quit      to quit administration connection");
      pw.println("shutdown  to shutdown cheater");
      pw.println("help      to show this message");
      pw.print(Prompt)
      pw.flush
      handleRequest(br, pw)
    case "shutdown" =>
      clientSocket.close
      adminServer.stop
      adminServer.getCheater.stopService
    case _ =>
      pw.println("Unknown command, use \"status|quit|shutdown|help\".")
      pw.print(Prompt)
      pw.flush
      handleRequest(br, pw)
  }

  def act {
    try {
      val br = new BufferedReader(new InputStreamReader(
        clientSocket.getInputStream))
      val pw = new PrintWriter(clientSocket.getOutputStream)

      pw.print(Prompt)
      pw.flush

      handleRequest(br, pw)
    } finally
      clientSocket.close
  }
}
