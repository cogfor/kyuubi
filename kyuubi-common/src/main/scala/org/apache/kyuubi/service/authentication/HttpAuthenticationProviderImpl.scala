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

import java.io.{BufferedReader, IOException, InputStream, InputStreamReader}

import org.apache.http.client.methods.{CloseableHttpResponse, HttpPost, HttpUriRequest}
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.{CloseableHttpClient, HttpClientBuilder}
import org.apache.kyuubi.Logging
import org.apache.kyuubi.config.KyuubiConf
import com.google.gson.{Gson, JsonArray, JsonElement, JsonObject, JsonParser}

/**
 * This authentication provider allows any combination of username and password.
 */
case class UserCred(username: String, password: String)

class HttpAuthenticationProviderImpl(conf: KyuubiConf) extends PasswdAuthenticationProvider with Logging {
  private val httpClientBuilder = HttpClientBuilder.create()

  private val sparkAppControlHost =
    conf.getOption("spark.application.operator.service.name").get
  private val sparkAppControlPort =
    conf.getOption("spark.application.operator.service.port").get

  private val sparkAppJwtAuthUrl = "/api/v1/auth/jwt"

  private var jwtToken: String = _

  def getJwtToken = jwtToken

  private def getSparkAppAuthtUrl() : String = {
    "http://" + sparkAppControlHost + ":" + sparkAppControlPort + sparkAppJwtAuthUrl
  }

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

  override def authenticate(user: String, password: String): Unit = {
    info(s"AnonymousAuthenticationProviderImpl : username=${user}, password=${password}")
    if (conf.getOption("spark.sql.engine").isDefined) {
      info("spark.sql.engine defined!")
      jwtToken = password
      return
    }

    // no-op authentication
    val client = httpClientBuilder.build()
    try {
      val authjsonstr = new Gson().toJson(UserCred(user, password))
      val auth = new HttpPost(getSparkAppAuthtUrl())
      auth.setHeader("Content-type", "application/json")
      auth.setEntity(new StringEntity(authjsonstr))
      val authRsp = doHttpExecute(client, auth)
      info(authRsp)

      val jsonobj = new JsonParser().parse(authRsp._2)
      if (jsonobj != null) {
        val token = jsonobj.getAsJsonObject()
          .getAsJsonObject("body")
          .getAsJsonObject("obj")
          .get("token").getAsString
        jwtToken = token
      } else {
        null
      }
    }
  }
}
