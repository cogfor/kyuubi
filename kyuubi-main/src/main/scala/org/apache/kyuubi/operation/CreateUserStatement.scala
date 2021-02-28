package org.apache.kyuubi.operation

import java.nio.ByteBuffer
import java.util
import java.util.ArrayList

import org.apache.hive.service.cli.{ColumnBasedSet, RowBasedSet}
import org.apache.hive.service.rpc.thrift
import org.apache.hive.service.rpc.thrift.TRowSet
import org.apache.kyuubi.operation.FetchOrientation.FetchOrientation
import org.apache.kyuubi.session.Session
import org.apache.spark.sql.internal.SQLConf
import spark.sql.dialect.executor.DialectCommandOperator
import spark.sql.dialect.handler.DefaultDialectHandler;


class CreateUserStatement(session: Session,
                          override val statement: String,
                          createUserOperator: DialectCommandOperator)
  extends KyuubiOperation(OperationType.EXECUTE_STATEMENT, session, null, null) {

  var result : String = null
  override protected def runInternal(): Unit = {
    //setState(OperationState.RUNNING)
    val handler = new DefaultDialectHandler(new SQLConf, createUserOperator)
    try {
      result = handler.sql(statement)
      _remoteOpHandle = null
      setState(OperationState.FINISHED)
    } catch {
      case e: Exception => {
        onError(e.getMessage)
      }
    }
  }

  override def getNextRowSet(order: FetchOrientation, rowSetSize: Int): TRowSet = {
    setHasResultSet(false)

    val ts1 = new org.apache.hive.service.cli.TableSchema()
    ts1.addStringColumn("result1","result of operation")
    val rbs1 = new RowBasedSet(ts1)
    //rbs1.addRow(Array[Object](result,result,result,result,result))

    val cbs1 = new ColumnBasedSet(ts1)
    cbs1.setStartOffset(0)
    cbs1.addRow(Array[Object](result))
    //cbs1.toTRowSet
    if (result != null) {
      result = null
      cbs1.toTRowSet
    } else {
      rbs1.toTRowSet
    }
  }
}
