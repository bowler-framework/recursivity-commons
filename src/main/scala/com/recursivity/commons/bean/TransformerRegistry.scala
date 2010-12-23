package com.recursivity.commons.bean
import collection.mutable.HashMap

/**
 * Created by IntelliJ IDEA.
 * User: wfaler
 * Date: Mar 25, 2010
 * Time: 11:25:17 PM
 * To change this template use File | Settings | File Templates.
 */

object TransformerRegistry{
  private val registry = new HashMap[Class[_], Class[_ <: StringValueTransformer]]
  registry += classOf[String] -> classOf[StringTransformer]
  registry += classOf[BigDecimal] -> classOf[BigDecimalTransformer]
  registry += classOf[Boolean] -> classOf[JavaBooleanTransformer]
  registry += classOf[java.util.Date] -> classOf[DateTransformer]
  registry += classOf[Int] -> classOf[JavaIntegerTransformer]
  registry += classOf[java.math.BigDecimal] -> classOf[JavaBigDecimalTransformer]
  registry += classOf[java.lang.Boolean] -> classOf[JavaBooleanTransformer]
  registry += classOf[java.lang.Integer] -> classOf[JavaIntegerTransformer]
  registry += classOf[java.lang.Long] -> classOf[LongTransformer]
  registry += classOf[java.lang.Double] -> classOf[DoubleTransformer]
  registry += classOf[java.lang.Float] -> classOf[FloatTransformer]
  registry += classOf[Long] -> classOf[LongTransformer]
  registry += classOf[Short] -> classOf[ShortTransformer]
  registry += classOf[java.lang.Short] -> classOf[ShortTransformer]


  def resolveTransformer(clazz: Class[_]): Option[StringValueTransformer] = {
    try{
      return Some(registry(clazz).newInstance)
    }catch{
      case e: NoSuchElementException => return None
    }
  }

  def registerTransformer(clazz: Class[_], transformerClass: Class[_<: StringValueTransformer]){
     registry += clazz -> transformerClass
  }
}