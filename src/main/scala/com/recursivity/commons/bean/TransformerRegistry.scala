package com.recursivity.commons.bean
import collection.mutable.HashMap

/**
 * Central registry for StringValueTransformers. transformers are registered and retrieved from this object.
 */

object TransformerRegistry{
  private val registry = new HashMap[Class[_], Class[_ <: StringValueTransformer[_]]]
  registry += classOf[String] -> classOf[StringTransformer]
  registry += classOf[BigDecimal] -> classOf[BigDecimalTransformer]
  registry += classOf[Boolean] -> classOf[JavaBooleanTransformer]
  registry += classOf[java.util.Date] -> classOf[DateTransformer]
  registry += classOf[Int] -> classOf[JavaIntegerTransformer]
  registry += classOf[Double] -> classOf[ScalaDoubleTransformer]
  registry += classOf[java.math.BigDecimal] -> classOf[JavaBigDecimalTransformer]
  registry += classOf[java.lang.Boolean] -> classOf[JavaBooleanTransformer]
  registry += classOf[java.lang.Integer] -> classOf[JavaIntegerTransformer]
  registry += classOf[java.lang.Long] -> classOf[LongTransformer]
  registry += classOf[java.lang.Double] -> classOf[DoubleTransformer]
  registry += classOf[java.lang.Float] -> classOf[FloatTransformer]
  registry += classOf[Long] -> classOf[LongTransformer]
  registry += classOf[Short] -> classOf[ShortTransformer]
  registry += classOf[Byte] -> classOf[ByteTransformer]
  registry += classOf[java.lang.Short] -> classOf[ShortTransformer]
  registry += classOf[java.lang.Byte] -> classOf[ByteTransformer]

  private val singletonRegistry = new HashMap[Class[_], StringValueTransformer[_]]


  def apply(clazz: Class[_]): Option[StringValueTransformer[_]] = {
    try{
      return Some(singletonRegistry(clazz))
    }catch{
      case ex: NoSuchElementException => {
        try{
          return Some(registry(clazz).newInstance)
        }catch{
          case e: NoSuchElementException => return None
        }
      }
    }
  }

  def registerTransformer(clazz: Class[_], transformerClass: Class[_<: StringValueTransformer[_]]){
     registry += clazz -> transformerClass
  }

  def registerSingletonTransformer(clazz: Class[_], transformer: StringValueTransformer[_]){
     singletonRegistry += clazz -> transformer
  }
}