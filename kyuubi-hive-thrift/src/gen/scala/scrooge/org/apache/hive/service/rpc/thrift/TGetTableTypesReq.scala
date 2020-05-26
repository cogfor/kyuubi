/**
 * Generated by Scrooge
 *   version: 19.12.0
 *   rev: dfdb68cf6b9c501dbbe3ae644504bf403ad76bfa
 *   built at: 20191212-171820
 */
package org.apache.hive.service.rpc.thrift

import com.twitter.io.Buf
import com.twitter.scrooge.{
  InvalidFieldsException,
  LazyTProtocol,
  StructBuilder,
  StructBuilderFactory,
  TFieldBlob,
  ThriftStruct,
  ThriftStructCodec3,
  ThriftStructField,
  ThriftStructFieldInfo,
  ThriftStructMetaData,
  ValidatingThriftStruct,
  ValidatingThriftStructCodec3
}
import org.apache.thrift.protocol._
import org.apache.thrift.transport.TMemoryBuffer
import scala.collection.immutable.{Map => immutable$Map}
import scala.collection.mutable.Builder
import scala.reflect.{ClassTag, classTag}


object TGetTableTypesReq extends ValidatingThriftStructCodec3[TGetTableTypesReq] with StructBuilderFactory[TGetTableTypesReq] {
  val NoPassthroughFields: immutable$Map[Short, TFieldBlob] = immutable$Map.empty[Short, TFieldBlob]
  val Struct: TStruct = new TStruct("TGetTableTypesReq")
  val SessionHandleField: TField = new TField("sessionHandle", TType.STRUCT, 1)
  val SessionHandleFieldManifest: Manifest[org.apache.hive.service.rpc.thrift.TSessionHandle] = implicitly[Manifest[org.apache.hive.service.rpc.thrift.TSessionHandle]]

  /**
   * Field information in declaration order.
   */
  lazy val fieldInfos: scala.List[ThriftStructFieldInfo] = scala.List[ThriftStructFieldInfo](
    new ThriftStructFieldInfo(
      SessionHandleField,
      false,
      true,
      SessionHandleFieldManifest,
      _root_.scala.None,
      _root_.scala.None,
      immutable$Map.empty[String, String],
      immutable$Map.empty[String, String],
      None
    )
  )

  lazy val structAnnotations: immutable$Map[String, String] =
    immutable$Map.empty[String, String]

  private val fieldTypes: IndexedSeq[ClassTag[_]] = IndexedSeq(
    classTag[org.apache.hive.service.rpc.thrift.TSessionHandle].asInstanceOf[ClassTag[_]]
  )

  private[this] val structFields: Seq[ThriftStructField[TGetTableTypesReq]] = {
    Seq(
      new ThriftStructField[TGetTableTypesReq](
        SessionHandleField,
        _root_.scala.Some(SessionHandleFieldManifest),
        classOf[TGetTableTypesReq]) {
          def getValue[R](struct: TGetTableTypesReq): R = struct.sessionHandle.asInstanceOf[R]
      }
    )
  }

  override lazy val metaData: ThriftStructMetaData[TGetTableTypesReq] =
    new ThriftStructMetaData(this, structFields, fieldInfos, Seq(), structAnnotations)

  /**
   * Checks that all required fields are non-null.
   */
  def validate(_item: TGetTableTypesReq): Unit = {
    if (_item.sessionHandle == null) throw new TProtocolException("Required field sessionHandle cannot be null")
  }

  /**
   * Checks that the struct is a valid as a new instance. If there are any missing required or
   * construction required fields, return a non-empty list.
   */
  def validateNewInstance(item: TGetTableTypesReq): scala.Seq[com.twitter.scrooge.validation.Issue] = {
    val buf = scala.collection.mutable.ListBuffer.empty[com.twitter.scrooge.validation.Issue]

    if (item.sessionHandle == null)
      buf += com.twitter.scrooge.validation.MissingRequiredField(fieldInfos.apply(0))
    buf ++= validateField(item.sessionHandle)
    buf.toList
  }

  def withoutPassthroughFields(original: TGetTableTypesReq): TGetTableTypesReq =
    new Immutable(
      sessionHandle =
        {
          val field = original.sessionHandle
          org.apache.hive.service.rpc.thrift.TSessionHandle.withoutPassthroughFields(field)
        }
    )

  def newBuilder(): StructBuilder[TGetTableTypesReq] = new TGetTableTypesReqStructBuilder(_root_.scala.None, fieldTypes)

  override def encode(_item: TGetTableTypesReq, _oproto: TProtocol): Unit = {
    _item.write(_oproto)
  }


  private[this] def lazyDecode(_iprot: LazyTProtocol): TGetTableTypesReq = {

    var sessionHandle: org.apache.hive.service.rpc.thrift.TSessionHandle = null
    var _got_sessionHandle = false

    var _passthroughFields: Builder[(Short, TFieldBlob), immutable$Map[Short, TFieldBlob]] = null
    var _done = false
    val _start_offset = _iprot.offset

    _iprot.readStructBegin()
    while (!_done) {
      val _field = _iprot.readFieldBegin()
      if (_field.`type` == TType.STOP) {
        _done = true
      } else {
        _field.id match {
          case 1 =>
            _field.`type` match {
              case TType.STRUCT =>
    
                sessionHandle = readSessionHandleValue(_iprot)
                _got_sessionHandle = true
              case _actualType =>
                val _expectedType = TType.STRUCT
                throw new TProtocolException(
                  "Received wrong type for field 'sessionHandle' (expected=%s, actual=%s).".format(
                    ttypeToString(_expectedType),
                    ttypeToString(_actualType)
                  )
                )
            }
          case _ =>
            if (_passthroughFields == null)
              _passthroughFields = immutable$Map.newBuilder[Short, TFieldBlob]
            _passthroughFields += (_field.id -> TFieldBlob.read(_field, _iprot))
        }
        _iprot.readFieldEnd()
      }
    }
    _iprot.readStructEnd()

    if (!_got_sessionHandle) throw new TProtocolException("Required field 'sessionHandle' was not found in serialized data for struct TGetTableTypesReq")
    new LazyImmutable(
      _iprot,
      _iprot.buffer,
      _start_offset,
      _iprot.offset,
      sessionHandle,
      if (_passthroughFields == null)
        NoPassthroughFields
      else
        _passthroughFields.result()
    )
  }

  override def decode(_iprot: TProtocol): TGetTableTypesReq =
    _iprot match {
      case i: LazyTProtocol => lazyDecode(i)
      case i => eagerDecode(i)
    }

  private[thrift] def eagerDecode(_iprot: TProtocol): TGetTableTypesReq = {
    var sessionHandle: org.apache.hive.service.rpc.thrift.TSessionHandle = null
    var _got_sessionHandle = false
    var _passthroughFields: Builder[(Short, TFieldBlob), immutable$Map[Short, TFieldBlob]] = null
    var _done = false

    _iprot.readStructBegin()
    while (!_done) {
      val _field = _iprot.readFieldBegin()
      if (_field.`type` == TType.STOP) {
        _done = true
      } else {
        _field.id match {
          case 1 =>
            _field.`type` match {
              case TType.STRUCT =>
                sessionHandle = readSessionHandleValue(_iprot)
                _got_sessionHandle = true
              case _actualType =>
                val _expectedType = TType.STRUCT
                throw new TProtocolException(
                  "Received wrong type for field 'sessionHandle' (expected=%s, actual=%s).".format(
                    ttypeToString(_expectedType),
                    ttypeToString(_actualType)
                  )
                )
            }
          case _ =>
            if (_passthroughFields == null)
              _passthroughFields = immutable$Map.newBuilder[Short, TFieldBlob]
            _passthroughFields += (_field.id -> TFieldBlob.read(_field, _iprot))
        }
        _iprot.readFieldEnd()
      }
    }
    _iprot.readStructEnd()

    if (!_got_sessionHandle) throw new TProtocolException("Required field 'sessionHandle' was not found in serialized data for struct TGetTableTypesReq")
    new Immutable(
      sessionHandle,
      if (_passthroughFields == null)
        NoPassthroughFields
      else
        _passthroughFields.result()
    )
  }

  def apply(
    sessionHandle: org.apache.hive.service.rpc.thrift.TSessionHandle
  ): TGetTableTypesReq =
    new Immutable(
      sessionHandle
    )

  def unapply(_item: TGetTableTypesReq): _root_.scala.Option[org.apache.hive.service.rpc.thrift.TSessionHandle] = _root_.scala.Some(_item.sessionHandle)


  @inline private[thrift] def readSessionHandleValue(_iprot: TProtocol): org.apache.hive.service.rpc.thrift.TSessionHandle = {
    org.apache.hive.service.rpc.thrift.TSessionHandle.decode(_iprot)
  }

  @inline private def writeSessionHandleField(sessionHandle_item: org.apache.hive.service.rpc.thrift.TSessionHandle, _oprot: TProtocol): Unit = {
    _oprot.writeFieldBegin(SessionHandleField)
    writeSessionHandleValue(sessionHandle_item, _oprot)
    _oprot.writeFieldEnd()
  }

  @inline private def writeSessionHandleValue(sessionHandle_item: org.apache.hive.service.rpc.thrift.TSessionHandle, _oprot: TProtocol): Unit = {
    sessionHandle_item.write(_oprot)
  }


  object Immutable extends ThriftStructCodec3[TGetTableTypesReq] {
    override def encode(_item: TGetTableTypesReq, _oproto: TProtocol): Unit = { _item.write(_oproto) }
    override def decode(_iprot: TProtocol): TGetTableTypesReq = TGetTableTypesReq.decode(_iprot)
    override lazy val metaData: ThriftStructMetaData[TGetTableTypesReq] = TGetTableTypesReq.metaData
  }

  /**
   * The default read-only implementation of TGetTableTypesReq.  You typically should not need to
   * directly reference this class; instead, use the TGetTableTypesReq.apply method to construct
   * new instances.
   */
  class Immutable(
      val sessionHandle: org.apache.hive.service.rpc.thrift.TSessionHandle,
      override val _passthroughFields: immutable$Map[Short, TFieldBlob])
    extends TGetTableTypesReq {
    def this(
      sessionHandle: org.apache.hive.service.rpc.thrift.TSessionHandle
    ) = this(
      sessionHandle,
      immutable$Map.empty[Short, TFieldBlob]
    )
  }

  /**
   * This is another Immutable, this however keeps strings as lazy values that are lazily decoded from the backing
   * array byte on read.
   */
  private[this] class LazyImmutable(
      _proto: LazyTProtocol,
      _buf: Array[Byte],
      _start_offset: Int,
      _end_offset: Int,
      val sessionHandle: org.apache.hive.service.rpc.thrift.TSessionHandle,
      override val _passthroughFields: immutable$Map[Short, TFieldBlob])
    extends TGetTableTypesReq {

    override def write(_oprot: TProtocol): Unit = {
      _oprot match {
        case i: LazyTProtocol => i.writeRaw(_buf, _start_offset, _end_offset - _start_offset)
        case _ => super.write(_oprot)
      }
    }


    /**
     * Override the super hash code to make it a lazy val rather than def.
     *
     * Calculating the hash code can be expensive, caching it where possible
     * can provide significant performance wins. (Key in a hash map for instance)
     * Usually not safe since the normal constructor will accept a mutable map or
     * set as an arg
     * Here however we control how the class is generated from serialized data.
     * With the class private and the contract that we throw away our mutable references
     * having the hash code lazy here is safe.
     */
    override lazy val hashCode = super.hashCode
  }

  /**
   * This Proxy trait allows you to extend the TGetTableTypesReq trait with additional state or
   * behavior and implement the read-only methods from TGetTableTypesReq using an underlying
   * instance.
   */
  trait Proxy extends TGetTableTypesReq {
    protected def _underlying_TGetTableTypesReq: TGetTableTypesReq
    override def sessionHandle: org.apache.hive.service.rpc.thrift.TSessionHandle = _underlying_TGetTableTypesReq.sessionHandle
    override def _passthroughFields: immutable$Map[Short, TFieldBlob] = _underlying_TGetTableTypesReq._passthroughFields
  }
}

/**
 * Prefer the companion object's [[org.apache.hive.service.rpc.thrift.TGetTableTypesReq.apply]]
 * for construction if you don't need to specify passthrough fields.
 */
trait TGetTableTypesReq
  extends ThriftStruct
  with _root_.scala.Product1[org.apache.hive.service.rpc.thrift.TSessionHandle]
  with ValidatingThriftStruct[TGetTableTypesReq]
  with java.io.Serializable
{
  import TGetTableTypesReq._

  def sessionHandle: org.apache.hive.service.rpc.thrift.TSessionHandle

  def _passthroughFields: immutable$Map[Short, TFieldBlob] = immutable$Map.empty

  def _1: org.apache.hive.service.rpc.thrift.TSessionHandle = sessionHandle


  /**
   * Gets a field value encoded as a binary blob using TCompactProtocol.  If the specified field
   * is present in the passthrough map, that value is returned.  Otherwise, if the specified field
   * is known and not optional and set to None, then the field is serialized and returned.
   */
  def getFieldBlob(_fieldId: Short): _root_.scala.Option[TFieldBlob] = {
    lazy val _buff = new TMemoryBuffer(32)
    lazy val _oprot = new TCompactProtocol(_buff)
    _passthroughFields.get(_fieldId) match {
      case blob: _root_.scala.Some[TFieldBlob] => blob
      case _root_.scala.None => {
        val _fieldOpt: _root_.scala.Option[TField] =
          _fieldId match {
            case 1 =>
              if (sessionHandle ne null) {
                writeSessionHandleValue(sessionHandle, _oprot)
                _root_.scala.Some(TGetTableTypesReq.SessionHandleField)
              } else {
                _root_.scala.None
              }
            case _ => _root_.scala.None
          }
        _fieldOpt match {
          case _root_.scala.Some(_field) =>
            _root_.scala.Some(TFieldBlob(_field, Buf.ByteArray.Owned(_buff.getArray())))
          case _root_.scala.None =>
            _root_.scala.None
        }
      }
    }
  }

  /**
   * Collects TCompactProtocol-encoded field values according to `getFieldBlob` into a map.
   */
  def getFieldBlobs(ids: TraversableOnce[Short]): immutable$Map[Short, TFieldBlob] =
    (ids flatMap { id => getFieldBlob(id) map { id -> _ } }).toMap

  /**
   * Sets a field using a TCompactProtocol-encoded binary blob.  If the field is a known
   * field, the blob is decoded and the field is set to the decoded value.  If the field
   * is unknown and passthrough fields are enabled, then the blob will be stored in
   * _passthroughFields.
   */
  def setField(_blob: TFieldBlob): TGetTableTypesReq = {
    var sessionHandle: org.apache.hive.service.rpc.thrift.TSessionHandle = this.sessionHandle
    var _passthroughFields = this._passthroughFields
    _blob.id match {
      case 1 =>
        sessionHandle = readSessionHandleValue(_blob.read)
      case _ => _passthroughFields += (_blob.id -> _blob)
    }
    new Immutable(
      sessionHandle,
      _passthroughFields
    )
  }

  /**
   * If the specified field is optional, it is set to None.  Otherwise, if the field is
   * known, it is reverted to its default value; if the field is unknown, it is removed
   * from the passthroughFields map, if present.
   */
  def unsetField(_fieldId: Short): TGetTableTypesReq = {
    var sessionHandle: org.apache.hive.service.rpc.thrift.TSessionHandle = this.sessionHandle

    _fieldId match {
      case 1 =>
        sessionHandle = null
      case _ =>
    }
    new Immutable(
      sessionHandle,
      _passthroughFields - _fieldId
    )
  }

  /**
   * If the specified field is optional, it is set to None.  Otherwise, if the field is
   * known, it is reverted to its default value; if the field is unknown, it is removed
   * from the passthroughFields map, if present.
   */
  def unsetSessionHandle: TGetTableTypesReq = unsetField(1)


  override def write(_oprot: TProtocol): Unit = {
    TGetTableTypesReq.validate(this)
    _oprot.writeStructBegin(Struct)
    if (sessionHandle ne null) writeSessionHandleField(sessionHandle, _oprot)
    if (_passthroughFields.nonEmpty) {
      _passthroughFields.values.foreach { _.write(_oprot) }
    }
    _oprot.writeFieldStop()
    _oprot.writeStructEnd()
  }

  def copy(
    sessionHandle: org.apache.hive.service.rpc.thrift.TSessionHandle = this.sessionHandle,
    _passthroughFields: immutable$Map[Short, TFieldBlob] = this._passthroughFields
  ): TGetTableTypesReq =
    new Immutable(
      sessionHandle,
      _passthroughFields
    )

  override def canEqual(other: Any): Boolean = other.isInstanceOf[TGetTableTypesReq]

  private def _equals(x: TGetTableTypesReq, y: TGetTableTypesReq): Boolean =
      x.productArity == y.productArity &&
      x.productIterator.sameElements(y.productIterator) &&
      x._passthroughFields == y._passthroughFields

  override def equals(other: Any): Boolean =
    canEqual(other) &&
      _equals(this, other.asInstanceOf[TGetTableTypesReq])

  override def hashCode: Int = {
    _root_.scala.runtime.ScalaRunTime._hashCode(this)
  }

  override def toString: String = _root_.scala.runtime.ScalaRunTime._toString(this)


  override def productArity: Int = 1

  override def productElement(n: Int): Any = n match {
    case 0 => this.sessionHandle
    case _ => throw new IndexOutOfBoundsException(n.toString)
  }

  override def productPrefix: String = "TGetTableTypesReq"

  def _codec: ValidatingThriftStructCodec3[TGetTableTypesReq] = TGetTableTypesReq

  def newBuilder(): StructBuilder[TGetTableTypesReq] = new TGetTableTypesReqStructBuilder(_root_.scala.Some(this), fieldTypes)
}

private[thrift] class TGetTableTypesReqStructBuilder(instance: _root_.scala.Option[TGetTableTypesReq], fieldTypes: IndexedSeq[ClassTag[_]])
    extends StructBuilder[TGetTableTypesReq](fieldTypes) {

  def build(): TGetTableTypesReq = instance match {
    case _root_.scala.Some(i) =>
      TGetTableTypesReq(
        (if (fieldArray(0) == null) i.sessionHandle else fieldArray(0)).asInstanceOf[org.apache.hive.service.rpc.thrift.TSessionHandle]
      )
    case _root_.scala.None =>
      if (fieldArray.contains(null)) throw new InvalidFieldsException(structBuildError("TGetTableTypesReq"))
      else {
        TGetTableTypesReq(
          fieldArray(0).asInstanceOf[org.apache.hive.service.rpc.thrift.TSessionHandle]
        )
      }
    }
}

