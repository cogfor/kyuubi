/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.kyuubi.service.authentication

import org.apache.hive.service.rpc.thrift.TCLIService.{Iface, Processor}
import org.apache.thrift.TException
import org.apache.thrift.protocol.TProtocol
import org.apache.thrift.transport.{TSaslClientTransport, TSaslServerTransport, TSocket, TTransport}

import org.apache.kyuubi.Logging

class TSetIpAddressProcessor[I <: Iface](
    iface: Iface) extends Processor[Iface](iface) with Logging {
  import TSetIpAddressProcessor._

  @throws[TException]
  override def process(in: TProtocol, out: TProtocol): Boolean = {
    setIpAddress(in)
    setUserName(in)
    try {
      super.process(in, out)
    } finally {
      THREAD_LOCAL_USER_NAME.remove()
      THREAD_LOCAL_IP_ADDRESS.remove()
    }
  }

  private def setUserName(in: TProtocol): Unit = {
    val transport = in.getTransport
    transport match {
      case transport1: TSaslServerTransport =>
        // TODO: 20210228 llz
        //val userName = transport1.getSaslServer.getAuthorizationID
        val userTokenStr = transport1.getSaslServer.getAuthorizationID
        val userToken = userTokenStr.split("\t")
        if (userToken.length >= 2) {
          THREAD_LOCAL_USER_NAME.set(userToken(0))
          THREAD_LOCAL_USER_JWT.set(userToken(1))
          //THREAD_LOCAL_USER_PW.set(userToken(1))
        } else {
          THREAD_LOCAL_USER_NAME.set(userToken(0))
        }
      case _ =>
    }
  }

  private def setIpAddress(in: TProtocol): Unit = {
    val transport = in.getTransport
    val tSocket = getUnderlyingSocketFromTransport(transport)
    if (tSocket == null) {
      warn("Unknown Transport, cannot determine ipAddress")
    } else {
      THREAD_LOCAL_IP_ADDRESS.set(tSocket.getSocket.getInetAddress.getHostAddress)
    }
  }

  @scala.annotation.tailrec
  private def getUnderlyingSocketFromTransport(transport: TTransport): TSocket = transport match {
    case t: TSaslServerTransport => getUnderlyingSocketFromTransport(t.getUnderlyingTransport)
    case t: TSaslClientTransport => getUnderlyingSocketFromTransport(t.getUnderlyingTransport)
    case t: TSocket => t
    case _ => null
  }

}

object TSetIpAddressProcessor {
  private val THREAD_LOCAL_IP_ADDRESS = new ThreadLocal[String]() {
    override protected def initialValue: String = null
  }
  private val THREAD_LOCAL_USER_NAME = new ThreadLocal[String]() {
    override protected def initialValue: String = null
  }
  private val THREAD_LOCAL_USER_PW = new ThreadLocal[String]() {
    override protected def initialValue: String = null
  }
  private val THREAD_LOCAL_USER_JWT = new ThreadLocal[String]() {
    override protected def initialValue: String = null
  }

  def getUserIpAddress: String = THREAD_LOCAL_IP_ADDRESS.get

  def getUserName: String = THREAD_LOCAL_USER_NAME.get

  def getUserPw: String = THREAD_LOCAL_USER_PW.get
  def getUserJwt: String = THREAD_LOCAL_USER_JWT.get
}
