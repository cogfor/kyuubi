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


object TGetSchemasResp extends ValidatingThriftStructCodec3[TGetSchemasResp] with StructBuilderFactory[TGetSchemasResp] {
  val NoPassthroughFields: immutable$Map[Short, TFieldBlob] = immutable$Map.empty[Short, TFieldBlob]
  val Struct: TStruct = new TStruct("TGetSchemasResp")
  val StatusField: TField = new TField("status", TType.STRUCT, 1)
  val StatusFieldManifest: Manifest[org.apache.hive.service.rpc.thrift.TStatus] = implicitly[Manifest[org.apache.hive.service.rpc.thrift.TStatus]]
  val OperationHandleField: TField = new TField("operationHandle", TType.STRUCT, 2)
  val OperationHandleFieldManifest: Manifest[org.apache.hive.service.rpc.thrift.TOperationHandle] = implicitly[Manifest[org.apache.hive.service.rpc.thrift.TOperationHandle]]

  /**
   * Field information in declaration order.
   */
  lazy val fieldInfos: scala.List[ThriftStructFieldInfo] = scala.List[ThriftStructFieldInfo](
    new ThriftStructFieldInfo(
      StatusField,
      false,
      true,
      StatusFieldManifest,
      _root_.scala.None,
      _root_.scala.None,
      immutable$Map.empty[String, String],
      immutable$Map.empty[String, String],
      None
    ),
    new ThriftStructFieldInfo(
      OperationHandleField,
      true,
      false,
      OperationHandleFieldManifest,
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
    classTag[org.apache.hive.service.rpc.thrift.TStatus].asInstanceOf[ClassTag[_]],
    classTag[_root_.scala.Option[org.apache.hive.service.rpc.thrift.TOperationHandle]].asInstanceOf[ClassTag[_]]
  )

  private[this] val structFields: Seq[ThriftStructField[TGetSchemasResp]] = {
    Seq(
      new ThriftStructField[TGetSchemasResp](
        StatusField,
        _root_.scala.Some(StatusFieldManifest),
        classOf[TGetSchemasResp]) {
          def getValue[R](struct: TGetSchemasResp): R = struct.status.asInstanceOf[R]
      },
      new ThriftStructField[TGetSchemasResp](
        OperationHandleField,
        _root_.scala.Some(OperationHandleFieldManifest),
        classOf[TGetSchemasResp]) {
          def getValue[R](struct: TGetSchemasResp): R = struct.operationHandle.asInstanceOf[R]
      }
    )
  }

  override lazy val metaData: ThriftStructMetaData[TGetSchemasResp] =
    new ThriftStructMetaData(this, structFields, fieldInfos, Seq(), structAnnotations)

  /**
   * Checks that all required fields are non-null.
   */
  def validate(_item: TGetSchemasResp): Unit = {
    if (_item.status == null) throw new TProtocolException("Required field status cannot be null")
  }

  /**
   * Checks that the struct is a valid as a new instance. If there are any missing required or
   * construction required fields, return a non-empty list.
   */
  def validateNewInstance(item: TGetSchemasResp): scala.Seq[com.twitter.scrooge.validation.Issue] = {
    val buf = scala.collection.mutable.ListBuffer.empty[com.twitter.scrooge.validation.Issue]

    if (item.status == null)
      buf += com.twitter.scrooge.validation.MissingRequiredField(fieldInfos.apply(0))
    buf ++= validateField(item.status)
    buf ++= validateField(item.operationHandle)
    buf.toList
  }

  def withoutPassthroughFields(original: TGetSchemasResp): TGetSchemasResp =
    new Immutable(
      status =
        {
          val field = original.status
          org.apache.hive.service.rpc.thrift.TStatus.withoutPassthroughFields(field)
        },
      operationHandle =
        {
          val field = original.operationHandle
          field.map { field =>
            org.apache.hive.service.rpc.thrift.TOperationHandle.withoutPassthroughFields(field)
          }
        }
    )

  def newBuilder(): StructBuilder[TGetSchemasResp] = new TGetSchemasRespStructBuilder(_root_.scala.None, fieldTypes)

  override def encode(_item: TGetSchemasResp, _oproto: TProtocol): Unit = {
    _item.write(_oproto)
  }


  private[this] def lazyDecode(_iprot: LazyTProtocol): TGetSchemasResp = {

    var status: org.apache.hive.service.rpc.thrift.TStatus = null
    var _got_status = false
    var operationHandle: Option[org.apache.hive.service.rpc.thrift.TOperationHandle] = None

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
    
                status = readStatusValue(_iprot)
                _got_status = true
              case _actualType =>
                val _expectedType = TType.STRUCT
                throw new TProtocolException(
                  "Received wrong type for field 'status' (expected=%s, actual=%s).".format(
                    ttypeToString(_expectedType),
                    ttypeToString(_actualType)
                  )
                )
            }
          case 2 =>
            _field.`type` match {
              case TType.STRUCT =>
    
                operationHandle = Some(readOperationHandleValue(_iprot))
              case _actualType =>
                val _expectedType = TType.STRUCT
                throw new TProtocolException(
                  "Received wrong type for field 'operationHandle' (expected=%s, actual=%s).".format(
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

    if (!_got_status) throw new TProtocolException("Required field 'status' was not found in serialized data for struct TGetSchemasResp")
    new LazyImmutable(
      _iprot,
      _iprot.buffer,
      _start_offset,
      _iprot.offset,
      status,
      operationHandle,
      if (_passthroughFields == null)
        NoPassthroughFields
      else
        _passthroughFields.result()
    )
  }

  override def decode(_iprot: TProtocol): TGetSchemasResp =
    _iprot match {
      case i: LazyTProtocol => lazyDecode(i)
      case i => eagerDecode(i)
    }

  private[thrift] def eagerDecode(_iprot: TProtocol): TGetSchemasResp = {
    var status: org.apache.hive.service.rpc.thrift.TStatus = null
    var _got_status = false
    var operationHandle: _root_.scala.Option[org.apache.hive.service.rpc.thrift.TOperationHandle] = _root_.scala.None
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
                status = readStatusValue(_iprot)
                _got_status = true
              case _actualType =>
                val _expectedType = TType.STRUCT
                throw new TProtocolException(
                  "Received wrong type for field 'status' (expected=%s, actual=%s).".format(
                    ttypeToString(_expectedType),
                    ttypeToString(_actualType)
                  )
                )
            }
          case 2 =>
            _field.`type` match {
              case TType.STRUCT =>
                operationHandle = _root_.scala.Some(readOperationHandleValue(_iprot))
              case _actualType =>
                val _expectedType = TType.STRUCT
                throw new TProtocolException(
                  "Received wrong type for field 'operationHandle' (expected=%s, actual=%s).".format(
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

    if (!_got_status) throw new TProtocolException("Required field 'status' was not found in serialized data for struct TGetSchemasResp")
    new Immutable(
      status,
      operationHandle,
      if (_passthroughFields == null)
        NoPassthroughFields
      else
        _passthroughFields.result()
    )
  }

  def apply(
    status: org.apache.hive.service.rpc.thrift.TStatus,
    operationHandle: _root_.scala.Option[org.apache.hive.service.rpc.thrift.TOperationHandle] = _root_.scala.None
  ): TGetSchemasResp =
    new Immutable(
      status,
      operationHandle
    )

  def unapply(_item: TGetSchemasResp): _root_.scala.Option[_root_.scala.Tuple2[org.apache.hive.service.rpc.thrift.TStatus, Option[org.apache.hive.service.rpc.thrift.TOperationHandle]]] = _root_.scala.Some(_item.toTuple)


  @inline private[thrift] def readStatusValue(_iprot: TProtocol): org.apache.hive.service.rpc.thrift.TStatus = {
    org.apache.hive.service.rpc.thrift.TStatus.decode(_iprot)
  }

  @inline private def writeStatusField(status_item: org.apache.hive.service.rpc.thrift.TStatus, _oprot: TProtocol): Unit = {
    _oprot.writeFieldBegin(StatusField)
    writeStatusValue(status_item, _oprot)
    _oprot.writeFieldEnd()
  }

  @inline private def writeStatusValue(status_item: org.apache.hive.service.rpc.thrift.TStatus, _oprot: TProtocol): Unit = {
    status_item.write(_oprot)
  }

  @inline private[thrift] def readOperationHandleValue(_iprot: TProtocol): org.apache.hive.service.rpc.thrift.TOperationHandle = {
    org.apache.hive.service.rpc.thrift.TOperationHandle.decode(_iprot)
  }

  @inline private def writeOperationHandleField(operationHandle_item: org.apache.hive.service.rpc.thrift.TOperationHandle, _oprot: TProtocol): Unit = {
    _oprot.writeFieldBegin(OperationHandleField)
    writeOperationHandleValue(operationHandle_item, _oprot)
    _oprot.writeFieldEnd()
  }

  @inline private def writeOperationHandleValue(operationHandle_item: org.apache.hive.service.rpc.thrift.TOperationHandle, _oprot: TProtocol): Unit = {
    operationHandle_item.write(_oprot)
  }


  object Immutable extends ThriftStructCodec3[TGetSchemasResp] {
    override def encode(_item: TGetSchemasResp, _oproto: TProtocol): Unit = { _item.write(_oproto) }
    override def decode(_iprot: TProtocol): TGetSchemasResp = TGetSchemasResp.decode(_iprot)
    override lazy val metaData: ThriftStructMetaData[TGetSchemasResp] = TGetSchemasResp.metaData
  }

  /**
   * The default read-only implementation of TGetSchemasResp.  You typically should not need to
   * directly reference this class; instead, use the TGetSchemasResp.apply method to construct
   * new instances.
   */
  class Immutable(
      val status: org.apache.hive.service.rpc.thrift.TStatus,
      val operationHandle: _root_.scala.Option[org.apache.hive.service.rpc.thrift.TOperationHandle],
      override val _passthroughFields: immutable$Map[Short, TFieldBlob])
    extends TGetSchemasResp {
    def this(
      status: org.apache.hive.service.rpc.thrift.TStatus,
      operationHandle: _root_.scala.Option[org.apache.hive.service.rpc.thrift.TOperationHandle] = _root_.scala.None
    ) = this(
      status,
      operationHandle,
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
      val status: org.apache.hive.service.rpc.thrift.TStatus,
      val operationHandle: _root_.scala.Option[org.apache.hive.service.rpc.thrift.TOperationHandle],
      override val _passthroughFields: immutable$Map[Short, TFieldBlob])
    extends TGetSchemasResp {

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
   * This Proxy trait allows you to extend the TGetSchemasResp trait with additional state or
   * behavior and implement the read-only methods from TGetSchemasResp using an underlying
   * instance.
   */
  trait Proxy extends TGetSchemasResp {
    protected def _underlying_TGetSchemasResp: TGetSchemasResp
    override def status: org.apache.hive.service.rpc.thrift.TStatus = _underlying_TGetSchemasResp.status
    override def operationHandle: _root_.scala.Option[org.apache.hive.service.rpc.thrift.TOperationHandle] = _underlying_TGetSchemasResp.operationHandle
    override def _passthroughFields: immutable$Map[Short, TFieldBlob] = _underlying_TGetSchemasResp._passthroughFields
  }
}

/**
 * Prefer the companion object's [[org.apache.hive.service.rpc.thrift.TGetSchemasResp.apply]]
 * for construction if you don't need to specify passthrough fields.
 */
trait TGetSchemasResp
  extends ThriftStruct
  with _root_.scala.Product2[org.apache.hive.service.rpc.thrift.TStatus, Option[org.apache.hive.service.rpc.thrift.TOperationHandle]]
  with ValidatingThriftStruct[TGetSchemasResp]
  with java.io.Serializable
{
  import TGetSchemasResp._

  def status: org.apache.hive.service.rpc.thrift.TStatus
  def operationHandle: _root_.scala.Option[org.apache.hive.service.rpc.thrift.TOperationHandle]

  def _passthroughFields: immutable$Map[Short, TFieldBlob] = immutable$Map.empty

  def _1: org.apache.hive.service.rpc.thrift.TStatus = status
  def _2: _root_.scala.Option[org.apache.hive.service.rpc.thrift.TOperationHandle] = operationHandle

  def toTuple: _root_.scala.Tuple2[org.apache.hive.service.rpc.thrift.TStatus, Option[org.apache.hive.service.rpc.thrift.TOperationHandle]] = {
    (
      status,
      operationHandle
    )
  }


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
              if (status ne null) {
                writeStatusValue(status, _oprot)
                _root_.scala.Some(TGetSchemasResp.StatusField)
              } else {
                _root_.scala.None
              }
            case 2 =>
              if (operationHandle.isDefined) {
                writeOperationHandleValue(operationHandle.get, _oprot)
                _root_.scala.Some(TGetSchemasResp.OperationHandleField)
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
  def setField(_blob: TFieldBlob): TGetSchemasResp = {
    var status: org.apache.hive.service.rpc.thrift.TStatus = this.status
    var operationHandle: _root_.scala.Option[org.apache.hive.service.rpc.thrift.TOperationHandle] = this.operationHandle
    var _passthroughFields = this._passthroughFields
    _blob.id match {
      case 1 =>
        status = readStatusValue(_blob.read)
      case 2 =>
        operationHandle = _root_.scala.Some(readOperationHandleValue(_blob.read))
      case _ => _passthroughFields += (_blob.id -> _blob)
    }
    new Immutable(
      status,
      operationHandle,
      _passthroughFields
    )
  }

  /**
   * If the specified field is optional, it is set to None.  Otherwise, if the field is
   * known, it is reverted to its default value; if the field is unknown, it is removed
   * from the passthroughFields map, if present.
   */
  def unsetField(_fieldId: Short): TGetSchemasResp = {
    var status: org.apache.hive.service.rpc.thrift.TStatus = this.status
    var operationHandle: _root_.scala.Option[org.apache.hive.service.rpc.thrift.TOperationHandle] = this.operationHandle

    _fieldId match {
      case 1 =>
        status = null
      case 2 =>
        operationHandle = _root_.scala.None
      case _ =>
    }
    new Immutable(
      status,
      operationHandle,
      _passthroughFields - _fieldId
    )
  }

  /**
   * If the specified field is optional, it is set to None.  Otherwise, if the field is
   * known, it is reverted to its default value; if the field is unknown, it is removed
   * from the passthroughFields map, if present.
   */
  def unsetStatus: TGetSchemasResp = unsetField(1)

  def unsetOperationHandle: TGetSchemasResp = unsetField(2)


  override def write(_oprot: TProtocol): Unit = {
    TGetSchemasResp.validate(this)
    _oprot.writeStructBegin(Struct)
    if (status ne null) writeStatusField(status, _oprot)
    if (operationHandle.isDefined) writeOperationHandleField(operationHandle.get, _oprot)
    if (_passthroughFields.nonEmpty) {
      _passthroughFields.values.foreach { _.write(_oprot) }
    }
    _oprot.writeFieldStop()
    _oprot.writeStructEnd()
  }

  def copy(
    status: org.apache.hive.service.rpc.thrift.TStatus = this.status,
    operationHandle: _root_.scala.Option[org.apache.hive.service.rpc.thrift.TOperationHandle] = this.operationHandle,
    _passthroughFields: immutable$Map[Short, TFieldBlob] = this._passthroughFields
  ): TGetSchemasResp =
    new Immutable(
      status,
      operationHandle,
      _passthroughFields
    )

  override def canEqual(other: Any): Boolean = other.isInstanceOf[TGetSchemasResp]

  private def _equals(x: TGetSchemasResp, y: TGetSchemasResp): Boolean =
      x.productArity == y.productArity &&
      x.productIterator.sameElements(y.productIterator) &&
      x._passthroughFields == y._passthroughFields

  override def equals(other: Any): Boolean =
    canEqual(other) &&
      _equals(this, other.asInstanceOf[TGetSchemasResp])

  override def hashCode: Int = {
    _root_.scala.runtime.ScalaRunTime._hashCode(this)
  }

  override def toString: String = _root_.scala.runtime.ScalaRunTime._toString(this)


  override def productArity: Int = 2

  override def productElement(n: Int): Any = n match {
    case 0 => this.status
    case 1 => this.operationHandle
    case _ => throw new IndexOutOfBoundsException(n.toString)
  }

  override def productPrefix: String = "TGetSchemasResp"

  def _codec: ValidatingThriftStructCodec3[TGetSchemasResp] = TGetSchemasResp

  def newBuilder(): StructBuilder[TGetSchemasResp] = new TGetSchemasRespStructBuilder(_root_.scala.Some(this), fieldTypes)
}

private[thrift] class TGetSchemasRespStructBuilder(instance: _root_.scala.Option[TGetSchemasResp], fieldTypes: IndexedSeq[ClassTag[_]])
    extends StructBuilder[TGetSchemasResp](fieldTypes) {

  def build(): TGetSchemasResp = instance match {
    case _root_.scala.Some(i) =>
      TGetSchemasResp(
        (if (fieldArray(0) == null) i.status else fieldArray(0)).asInstanceOf[org.apache.hive.service.rpc.thrift.TStatus],
        (if (fieldArray(1) == null) i.operationHandle else fieldArray(1)).asInstanceOf[_root_.scala.Option[org.apache.hive.service.rpc.thrift.TOperationHandle]]
      )
    case _root_.scala.None =>
      if (fieldArray.contains(null)) throw new InvalidFieldsException(structBuildError("TGetSchemasResp"))
      else {
        TGetSchemasResp(
          fieldArray(0).asInstanceOf[org.apache.hive.service.rpc.thrift.TStatus],
          fieldArray(1).asInstanceOf[_root_.scala.Option[org.apache.hive.service.rpc.thrift.TOperationHandle]]
        )
      }
    }
}

