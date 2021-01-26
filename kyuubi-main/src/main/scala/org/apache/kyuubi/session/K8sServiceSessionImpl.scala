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

import java.io.{InputStream, IOException}
import java.nio.charset.StandardCharsets
import java.util.concurrent.TimeUnit

import scala.collection.JavaConverters._

import org.apache.curator.utils.ZKPaths
import org.apache.hive.service.rpc.thrift._
import org.apache.http.client.methods.{CloseableHttpResponse, HttpDelete, HttpGet, HttpPost, HttpUriRequest}
import org.apache.http.impl.client.{CloseableHttpClient, DefaultHttpClient, HttpClientBuilder}
import org.apache.http.entity.StringEntity

import org.apache.thrift.TException
import org.apache.thrift.protocol.TBinaryProtocol
import org.apache.thrift.transport.{TSocket, TTransport}

import com.google.gson.{Gson, JsonArray, JsonElement, JsonObject, JsonParser}

import org.apache.kyuubi.{KyuubiSQLException, ThriftUtils}
import org.apache.kyuubi.config.KyuubiConf
import org.apache.kyuubi.config.KyuubiConf._
import org.apache.kyuubi.engine.spark.SparkProcessBuilder
import org.apache.kyuubi.service.authentication.PlainSASLHelper

case class UserCred(username: String, password: String)
class SparkSqlServiceException(code : Int, message : String)
  extends IOException("error code : " + code + "\n" + message)

case class SparkApplicationConnException(msg: String) extends
  SparkSqlServiceException(10000, msg);
case class SparkApplicationInternalException(msg: String) extends
  SparkSqlServiceException(10001, msg);

case class SparkApplicationAuthException(msg: String) extends
  SparkSqlServiceException(10002, msg);
case class SparkApplicationCreateException(msg: String) extends
  SparkSqlServiceException(10003, msg);
case class SparkApplicationStateException(msg: String) extends
  SparkSqlServiceException(10004, msg);
case class SparkApplicationDeleteException(msg: String) extends
  SparkSqlServiceException(10005, msg);

case class SparkResourceApply(drivercores: Int,
                              drivermem: String,
                              exectorcores: Int,
                              exectormem: String,
                              extraconf: java.util.Map[String,String])

class K8sServiceSessionImpl(
    protocol: TProtocolVersion,
    user: String,
    password: String,
    ipAddress: String,
    conf: Map[String, String],
    sessionManager: K8sServiceSessionManager,
    sessionConf: KyuubiConf)
  extends AbstractSession(protocol, user, password, ipAddress, conf, sessionManager) {

  private def configureSession(): Unit = {
    conf.foreach {
      case (HIVE_VAR_PREFIX(key), value) => sessionConf.set(key, value)
      case (HIVE_CONF_PREFIX(key), value) => sessionConf.set(key, value)
      case ("use:database", _) =>
      case (key, value) => sessionConf.set(key, value)
    }
  }

  private val timeout: Long = sessionConf.get(ENGINE_INIT_TIMEOUT)
  // TODO: changed by llz 20210105
  private var transport: TTransport = _
  private var client: TCLIService.Client = _
  private var remoteSessionHandle: TSessionHandle = _

  private val httpClientBuilder = HttpClientBuilder.create()

  private val sparkAppControlHost =
    sessionConf.getOption("spark.application.operator.service.name").get
  private val sparkAppControlPort =
    sessionConf.getOption("spark.application.operator.service.port").get
  private val sparkAppDefaultUrl = "/api/v1/k8s/app/spark-sql/engine"
  private val sparkAppJwtAuthUrl = "/api/v1/auth/jwt"
  private def getSparkAppDefaultUrl() : String = {
    "http://" + sparkAppControlHost + ":" + sparkAppControlPort + sparkAppDefaultUrl
  }
  private def getSparkAppAuthtUrl() : String = {
    "http://" + sparkAppControlHost + ":" + sparkAppControlPort + sparkAppJwtAuthUrl
  }

  import java.io.BufferedReader
  import java.io.IOException
  import java.io.InputStreamReader

  private def convertStreamToString(is: InputStream) : String = {
    val reader = new BufferedReader(new InputStreamReader(is))
    val sb = new StringBuilder
    var line : String = null
    try {
      while (true) {
        line = reader.readLine()
        if (line == null) {
          throw new IOException("over")
        }
        sb.append(line + "\n")
      }
    } catch {
      case e: IOException =>
      // e.printStackTrace()
    } finally {
      try {
        is.close
      } catch {
        case e: IOException =>
          e.printStackTrace()
      }
    }

    sb.toString
  }

  def doHttpExecute(client : CloseableHttpClient, req: HttpUriRequest): (Int, String) = {
    var info : String = null
    var resp : CloseableHttpResponse = null
    var code = 0
    try {
      resp = client.execute(req)
      code = resp.getStatusLine.getStatusCode
      if (resp.getEntity == null) {
        throw new IOException("response is empty!")
      }
      info = convertStreamToString(resp.getEntity.getContent)
    } finally {
      if (resp != null) {
        resp.close()
      }
    }

    (code, info)
  }

  @throws [SparkSqlServiceException]
  private def throwBasicException(code: Int, msg: String): Unit = {
    if (code == 0) {
      throw SparkApplicationConnException(msg)
    }
    if (code >= 400 && code < 500) {
      throw SparkApplicationAuthException(msg)
    }
  }

  private def throwAuthException(code: Int, msg: String): Unit = {
    throwBasicException(code, msg)
    if (code >= 500) {
      throw SparkApplicationInternalException(msg)
    }
  }

  private def throwCreateException(code: Int, msg: String): Unit = {
    throwBasicException(code, msg)
    if (code >= 500) {
      throw SparkApplicationCreateException(msg)
    }
  }

  private def throwStateException(code: Int, msg: String): Unit = {
    throwBasicException(code, msg)
    if (code >= 500) {
      throw SparkApplicationStateException(msg)
    }
  }

  private def throwDeleteException(code: Int, msg: String): Unit = {
    throwBasicException(code, msg)
    if (code >= 500) {
      throw SparkApplicationDeleteException(msg)
    }
  }

  @throws [SparkSqlServiceException]
  private def doDeleteSqlService(user: String, password: String) : Unit = {
    val client = httpClientBuilder.build()
    try {
      val authjsonstr = new Gson().toJson(UserCred(user, password))
      val auth = new HttpPost(getSparkAppAuthtUrl())
      auth.setHeader("Content-type", "application/json")
      auth.setEntity(new StringEntity(authjsonstr))
      val authRsp = doHttpExecute(client, auth)
      info(authRsp)

      throwAuthException(authRsp._1, authRsp._2)

      val jsonobj = new JsonParser().parse(authRsp._2)
      if (jsonobj != null) {
        val token = jsonobj.getAsJsonObject()
          .getAsJsonObject("body")
          .getAsJsonObject("obj")
          .get("token").getAsString

        val del = new HttpDelete(getSparkAppDefaultUrl())
        del.setHeader("Content-type", "application/json")
        del.addHeader("Authorization", "Bearer " + token)
        val delRsp = doHttpExecute(client, del)
        info(delRsp)

        throwDeleteException(delRsp._1, delRsp._2)
      }
    } catch {
      case e: SparkSqlServiceException => throw e
      case e: Throwable =>
        e.printStackTrace()
        throw SparkApplicationInternalException(e.getMessage)
    } finally {
      client.close()
    }
  }

  @throws [SparkSqlServiceException]
  private def doStartSqlService(user: String,
                                password: String,
                                resource: SparkResourceApply) : (String, Int) = {
    val client = httpClientBuilder.build()
    info(s" user jdbc request param : user=${user}, resource=${resource}, httpPostJson=${new Gson().toJson(resource)}")
    try {
      val authjsonstr = new Gson().toJson(UserCred(user, password))
      val auth = new HttpPost(getSparkAppAuthtUrl())
      auth.setHeader("Content-type", "application/json")
      auth.setEntity(new StringEntity(authjsonstr))
      val authRsp = doHttpExecute(client, auth)
      info(authRsp)

      throwAuthException(authRsp._1, authRsp._2)

      val jsonobj = new JsonParser().parse(authRsp._2)
      if (jsonobj != null) {
        val token = jsonobj.getAsJsonObject()
          .getAsJsonObject("body")
          .getAsJsonObject("obj")
          .get("token").getAsString

        val create = new HttpPost(getSparkAppDefaultUrl())
        create.setHeader("Content-type", "application/json")
        create.addHeader("Authorization", "Bearer " + token)
        create.setEntity(new StringEntity(new Gson().toJson(resource)))
        val createRsp = doHttpExecute(client, create)
        info(createRsp)

        throwCreateException(createRsp._1, createRsp._2)

        val creatJson = new JsonParser().parse(createRsp._2)
        if (creatJson != null) {
          val ip = creatJson.getAsJsonObject()
            .getAsJsonObject("body")
            .getAsJsonObject("obj")
            .get("serviceip").getAsString
          val port = creatJson.getAsJsonObject()
            .getAsJsonObject("body")
            .getAsJsonObject("obj")
            .get("serviceport").getAsString.toInt

          val state = new HttpGet(getSparkAppDefaultUrl())
          state.setHeader("Content-type", "application/json")
          state.addHeader("Authorization", "Bearer " + token)
          var stateinfo = ""
          var count = 0
          val WAIT_SECOND = 120
          //
          info("start waiting 120s to start sql service...")
          while(!stateinfo.equalsIgnoreCase("RUNNING") && count < WAIT_SECOND) {
            val stateRsp = doHttpExecute(client, state)
            throwStateException(stateRsp._1, stateRsp._2)

            val stateJson = new JsonParser().parse(stateRsp._2)
            if (stateJson == null) {
              throw SparkApplicationStateException("query spark sql service state error!")
            }

            debug(stateJson)

            stateinfo = stateJson.getAsJsonObject()
              .getAsJsonObject("body")
              .getAsJsonObject("obj")
              .getAsJsonObject("state")
              .get("state").getAsString

            count = count + 1
            Thread.sleep(1000)
          }

          if (!stateinfo.equalsIgnoreCase("RUNNING")) {
            throw SparkApplicationStateException("staring spark sql service, please wait a minute!")
          }

          (ip, port)
        } else {
          throw SparkApplicationCreateException("create spark sql, response is wrong!")
        }
      } else {
        throw new SparkSqlServiceException(10000 + authRsp._1, "auth response is wrong!")
      }
    } catch {
      case e: SparkSqlServiceException => throw e
      case e: Throwable =>
        e.printStackTrace()
        throw SparkApplicationInternalException(e.getMessage)
    } finally {
      client.close()
    }
  }

  def shutdownSparkSqlService(user: String, password: String) : Unit = {
    doDeleteSqlService(user, password)
  }

  def getSparkResourceApply(conf: Map[String, String]) : SparkResourceApply = {
    var drivercores = conf.get("drivercores").getOrElse("1").toInt
    var drivermem = conf.get("drivermem").getOrElse("512m")
    var executorcores = conf.get("executorcores").getOrElse("1").toInt
    var executormem = conf.get("executormem").getOrElse("512m")

    // val confMap = new scala.collection.mutable.HashMap[String,String]
    val confMap = new java.util.HashMap[String,String]

    conf.foreach(kv => {
      if (kv._1.startsWith("set:hiveconf:")) {
        confMap.put(kv._1.substring("set:hiveconf:".length), kv._2)
      }
    })
    // isDefined
    if (confMap.containsKey("spark.executor.cores")) {
      executorcores = confMap.get("spark.executor.cores").toInt
    }

    if (confMap.containsKey("spark.executor.memory")) {
      executormem = confMap.get("spark.executor.memory")
    }

    if (confMap.containsKey("spark.driver.cores")) {
      drivercores = confMap.get("spark.driver.cores").toInt
    }

    if (confMap.containsKey("spark.driver.memory")) {
      drivermem = confMap.get("spark.driver.memory")
    }

    val resource = SparkResourceApply(drivercores,
      drivermem, executorcores, executormem, confMap)

    resource
  }

  override def open(): Unit = {
    super.open()
    configureSession()

    try {
      val ipport = doStartSqlService(user, password, getSparkResourceApply(conf))
      openSession(ipport._1, ipport._2)
    } catch {
      case e: SparkSqlServiceException =>
        error("Failed to open spark sql service ", e)
        throw e
    }
  }

  private def openSession(host: String, port: Int): Unit = {
    val passwd = Option(password).filter(_.nonEmpty).getOrElse("anonymous")
    val loginTimeout = sessionConf.get(ENGINE_LOGIN_TIMEOUT).toInt
    transport = PlainSASLHelper.getPlainTransport(
      user, passwd, new TSocket(host, port, loginTimeout))
    // TODO: added 20210116
    var isOk = false
    var retry = 0
    val maxRetry = 30
    while (!isOk && retry < maxRetry) {
      retry = retry + 1
      try {
        if (!transport.isOpen) transport.open()
        isOk = true
      } catch {
        case e: Exception =>
          error(s"try ${retry} , connecting ${host}:${port} failed!", e)
          if (retry >= maxRetry) {
            throw  e
          } else {
            Thread.sleep(2000)
          }
      }
    }
    client = new TCLIService.Client(new TBinaryProtocol(transport))
    val req = new TOpenSessionReq()
    req.setUsername(user)
    req.setPassword(passwd)
    req.setConfiguration(conf.asJava)
    val resp = client.OpenSession(req)
    ThriftUtils.verifyTStatus(resp.getStatus)
    remoteSessionHandle = resp.getSessionHandle
    sessionManager.operationManager.setConnection(handle, client, remoteSessionHandle)
  }

  override def close(): Unit = {
    super.close()
    sessionManager.operationManager.removeConnection(handle)
    try {
      if (remoteSessionHandle != null) {
        val req = new TCloseSessionReq(remoteSessionHandle)
        val resp = client.CloseSession(req)
        ThriftUtils.verifyTStatus(resp.getStatus)
      }
    } catch {
      case e: TException =>
        throw KyuubiSQLException("Error while cleaning up the engine resources", e)
    } finally {
      client = null
      if (transport != null) {
        transport.close()
      }
    }
  }
}
