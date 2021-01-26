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

package org.apache.kyuubi.session

import java.util.concurrent.TimeUnit

import org.apache.hive.service.rpc.thrift.TProtocolVersion

import org.apache.kyuubi.KyuubiSQLException
import org.apache.kyuubi.config.KyuubiConf
import org.apache.kyuubi.operation.KyuubiOperationManager

class K8sServiceSessionManager private(name: String) extends SessionManager(name) {

  def this() = this(classOf[K8sServiceSessionManager].getSimpleName)

  val operationManager = new KyuubiOperationManager()

  override def initialize(conf: KyuubiConf): Unit = {
    super.initialize(conf)
  }

  override def start(): Unit = {
    super.start()
  }

  override def openSession(
      protocol: TProtocolVersion,
      user: String,
      password: String,
      ipAddress: String,
      conf: Map[String, String]): SessionHandle = {

    val username = Option(user).filter(_.nonEmpty).getOrElse("anonymous")

    val sessionImpl = new K8sServiceSessionImpl(
      protocol,
      username,
      password,
      ipAddress,
      conf,
      this,
      this.getConf.getUserDefaults(user))
    val handle = sessionImpl.handle
    try {
      sessionImpl.open()
      setSession(handle, sessionImpl)
      info(s"$username's session with $handle is opened, current opening sessions" +
      s" $getOpenSessionCount")
      handle
    } catch {
      case e: Throwable =>
        e.printStackTrace()
        error(e)
        try {
          sessionImpl.close()
        } catch {
          case t: Throwable => warn(s"Error closing session $handle for $username", t)
        }
        throw KyuubiSQLException(
          s"Error opening session $handle for $username due to ${e.getMessage}", e)
    }
  }

  override def closeSession(sessionHandle: SessionHandle) : Unit = {
    // val session : K8sServiceSessionImpl = getSession(sessionHandle)
    //  .asInstanceOf[K8sServiceSessionImpl]
    // session.shutdownSparkSqlService()

    super.closeSession(sessionHandle)
  }

  override protected def isServer: Boolean = true
}
