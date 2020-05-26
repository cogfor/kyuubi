/**
 * Generated by Scrooge
 *   version: 19.12.0
 *   rev: dfdb68cf6b9c501dbbe3ae644504bf403ad76bfa
 *   built at: 20191212-171820
 */
package org.apache.hive.service.rpc.thrift

import com.twitter.scrooge.ThriftEnum
import scala.collection.immutable.{Map => immutable$Map}


@javax.annotation.Generated(value = Array("com.twitter.scrooge.Compiler"))
case object TFetchOrientation extends _root_.com.twitter.scrooge.ThriftEnumObject[TFetchOrientation] {

  val annotations: immutable$Map[String, String] = immutable$Map.empty

  
  case object FetchNext extends org.apache.hive.service.rpc.thrift.TFetchOrientation {
    val value: Int = 0
    val name: String = "FetchNext"
    val originalName: String = "FETCH_NEXT"
    val annotations: immutable$Map[String, String] = immutable$Map.empty
  }

  private[this] val _SomeFetchNext = _root_.scala.Some(org.apache.hive.service.rpc.thrift.TFetchOrientation.FetchNext)
  
  case object FetchPrior extends org.apache.hive.service.rpc.thrift.TFetchOrientation {
    val value: Int = 1
    val name: String = "FetchPrior"
    val originalName: String = "FETCH_PRIOR"
    val annotations: immutable$Map[String, String] = immutable$Map.empty
  }

  private[this] val _SomeFetchPrior = _root_.scala.Some(org.apache.hive.service.rpc.thrift.TFetchOrientation.FetchPrior)
  
  case object FetchRelative extends org.apache.hive.service.rpc.thrift.TFetchOrientation {
    val value: Int = 2
    val name: String = "FetchRelative"
    val originalName: String = "FETCH_RELATIVE"
    val annotations: immutable$Map[String, String] = immutable$Map.empty
  }

  private[this] val _SomeFetchRelative = _root_.scala.Some(org.apache.hive.service.rpc.thrift.TFetchOrientation.FetchRelative)
  
  case object FetchAbsolute extends org.apache.hive.service.rpc.thrift.TFetchOrientation {
    val value: Int = 3
    val name: String = "FetchAbsolute"
    val originalName: String = "FETCH_ABSOLUTE"
    val annotations: immutable$Map[String, String] = immutable$Map.empty
  }

  private[this] val _SomeFetchAbsolute = _root_.scala.Some(org.apache.hive.service.rpc.thrift.TFetchOrientation.FetchAbsolute)
  
  case object FetchFirst extends org.apache.hive.service.rpc.thrift.TFetchOrientation {
    val value: Int = 4
    val name: String = "FetchFirst"
    val originalName: String = "FETCH_FIRST"
    val annotations: immutable$Map[String, String] = immutable$Map.empty
  }

  private[this] val _SomeFetchFirst = _root_.scala.Some(org.apache.hive.service.rpc.thrift.TFetchOrientation.FetchFirst)
  
  case object FetchLast extends org.apache.hive.service.rpc.thrift.TFetchOrientation {
    val value: Int = 5
    val name: String = "FetchLast"
    val originalName: String = "FETCH_LAST"
    val annotations: immutable$Map[String, String] = immutable$Map.empty
  }

  private[this] val _SomeFetchLast = _root_.scala.Some(org.apache.hive.service.rpc.thrift.TFetchOrientation.FetchLast)

  case class EnumUnknownTFetchOrientation(value: Int)
    extends org.apache.hive.service.rpc.thrift.TFetchOrientation with _root_.com.twitter.scrooge.EnumItemUnknown
  {
    val name: String = "EnumUnknownTFetchOrientation" + value
    def originalName: String = name
    val annotations: immutable$Map[String, String] = immutable$Map.empty
  }

  /**
   * Find the enum by its integer value, as defined in the Thrift IDL.
   */
  def apply(value: Int): org.apache.hive.service.rpc.thrift.TFetchOrientation =
    value match {
      case 0 => org.apache.hive.service.rpc.thrift.TFetchOrientation.FetchNext
      case 1 => org.apache.hive.service.rpc.thrift.TFetchOrientation.FetchPrior
      case 2 => org.apache.hive.service.rpc.thrift.TFetchOrientation.FetchRelative
      case 3 => org.apache.hive.service.rpc.thrift.TFetchOrientation.FetchAbsolute
      case 4 => org.apache.hive.service.rpc.thrift.TFetchOrientation.FetchFirst
      case 5 => org.apache.hive.service.rpc.thrift.TFetchOrientation.FetchLast
      case _ => throw new NoSuchElementException(value.toString)
    }

  /**
   * Find the enum by its integer value, as defined in the Thrift IDL.
   * returns an EnumUnknownTFetchOrientation(value) if the value is not found.
   * In particular this allows ignoring new values added to an enum
   * in the IDL on the producer side when the consumer was not updated.
   */
  def getOrUnknown(value: Int): org.apache.hive.service.rpc.thrift.TFetchOrientation =
    get(value) match {
      case _root_.scala.Some(e) => e
      case _root_.scala.None => EnumUnknownTFetchOrientation(value)
    }

  /**
   * Find the enum by its integer value, as defined in the Thrift IDL.
   * Returns None if the value is not found
   */
  def get(value: Int): _root_.scala.Option[org.apache.hive.service.rpc.thrift.TFetchOrientation] =
    value match {
      case 0 => _SomeFetchNext
      case 1 => _SomeFetchPrior
      case 2 => _SomeFetchRelative
      case 3 => _SomeFetchAbsolute
      case 4 => _SomeFetchFirst
      case 5 => _SomeFetchLast
      case _ => _root_.scala.None
    }

  def valueOf(name: String): _root_.scala.Option[org.apache.hive.service.rpc.thrift.TFetchOrientation] =
    name.toLowerCase match {
      case "fetchnext" => _SomeFetchNext
      case "fetchprior" => _SomeFetchPrior
      case "fetchrelative" => _SomeFetchRelative
      case "fetchabsolute" => _SomeFetchAbsolute
      case "fetchfirst" => _SomeFetchFirst
      case "fetchlast" => _SomeFetchLast
      case _ => _root_.scala.None
    }

  lazy val list: List[org.apache.hive.service.rpc.thrift.TFetchOrientation] = scala.List[org.apache.hive.service.rpc.thrift.TFetchOrientation](
    org.apache.hive.service.rpc.thrift.TFetchOrientation.FetchNext,
    org.apache.hive.service.rpc.thrift.TFetchOrientation.FetchPrior,
    org.apache.hive.service.rpc.thrift.TFetchOrientation.FetchRelative,
    org.apache.hive.service.rpc.thrift.TFetchOrientation.FetchAbsolute,
    org.apache.hive.service.rpc.thrift.TFetchOrientation.FetchFirst,
    org.apache.hive.service.rpc.thrift.TFetchOrientation.FetchLast
  )
}



@javax.annotation.Generated(value = Array("com.twitter.scrooge.Compiler"))
sealed trait TFetchOrientation extends ThriftEnum with Serializable