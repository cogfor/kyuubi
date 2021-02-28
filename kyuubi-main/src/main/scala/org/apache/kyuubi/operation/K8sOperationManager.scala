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

package org.apache.kyuubi.operation

import java.util
import java.util.concurrent.ConcurrentHashMap

import org.apache.hive.service.rpc.thrift.{TCLIService, TFetchResultsReq, TRowSet, TSessionHandle}
import org.apache.kyuubi.operation.FetchOrientation.FetchOrientation
import org.apache.kyuubi.session.{K8sServiceSessionImpl, Session, SessionHandle}
import org.apache.kyuubi.{KyuubiSQLException, ThriftUtils}
import spark.sql.dialect.executor.DialectCommandOperator

class CreateUserOperator(session: Session) extends DialectCommandOperator {
  override def createUser(name: String, password: String, sets: util.Map[String, String]): String = {
    session.asInstanceOf[K8sServiceSessionImpl].createUser(name, password, sets)
  }
}

class K8sOperationManager(name: String) extends KyuubiOperationManager(name) {

  def this() = this(classOf[K8sOperationManager].getSimpleName)

  val ADMIN_USER_NAME = "admin"
  private def isAdministratorUser(user: String): Boolean = {
    user.equals(ADMIN_USER_NAME)
  }

  private def isCreateUserCommand(statement: String): Boolean = {
    (statement.trim.split(";").length < 2) && statement.trim.toLowerCase().startsWith("create user ")
  }

  override def newExecuteStatementOperation(
      session: Session,
      statement: String,
      runAsync: Boolean,
      queryTimeout: Long): Operation = {
    if (isCreateUserCommand(statement) && isAdministratorUser(session.user)) {
      val operation = new CreateUserStatement(session, statement, new CreateUserOperator(session))
      info(s"newExecuteStatementOp, add op : ${operation.getHandle}")
      addOperation(operation)
    } else {
      super.newExecuteStatementOperation(session, statement, runAsync, queryTimeout)
    }
  }

  override def newGetTypeInfoOperation(session: Session): Operation = {
    val client = getThriftClient(session.handle)
    val remoteSessionHandle = getRemoteTSessionHandle(session.handle)
    val operation = new GetTypeInfo(session, client, remoteSessionHandle)
    addOperation(operation)
  }
}
