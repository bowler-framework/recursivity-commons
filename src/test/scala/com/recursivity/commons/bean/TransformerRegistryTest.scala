package com.recursivity.commons.bean

import org.scalatest.FunSuite
import java.text.SimpleDateFormat
import java.util.Date

/**
 * Created by IntelliJ IDEA.
 * User: wfaler
 * Date: Mar 26, 2010
 * Time: 12:15:53 AM
 * To change this template use File | Settings | File Templates.
 */

class TransformerRegistryTest extends FunSuite {



  test("RegistryKeys") {
    assert(classOf[BigDecimalTransformer].equals(TransformerRegistry(classOf[BigDecimal]).get.getClass))
    assert(classOf[JavaBooleanTransformer].equals(TransformerRegistry(classOf[Boolean]).get.getClass))
    assert(classOf[DateTransformer].equals(TransformerRegistry(classOf[java.util.Date]).get.getClass))
    assert(classOf[JavaBigDecimalTransformer].equals(TransformerRegistry(classOf[java.math.BigDecimal]).get.getClass))
    assert(classOf[JavaBooleanTransformer].equals(TransformerRegistry(classOf[java.lang.Boolean]).get.getClass))
    assert(classOf[JavaIntegerTransformer].equals(TransformerRegistry(classOf[java.lang.Integer]).get.getClass))
    assert(classOf[LongTransformer].equals(TransformerRegistry(classOf[java.lang.Long]).get.getClass))
    assert(classOf[LongTransformer].equals(TransformerRegistry(classOf[Long]).get.getClass))
    assert(classOf[StringTransformer].equals(TransformerRegistry(classOf[String]).get.getClass))

  }

  test("BigDecimalTransformer") {
    val transformer = new BigDecimalTransformer
    val decimal = new BigDecimal(new java.math.BigDecimal("14.51"))
    assert(transformer.toValue("0000000000000014.51").get.equals(decimal))

  }

  test("JavaBigDecimalTransformer") {
    val transformer = new JavaBigDecimalTransformer

    val decimal = new java.math.BigDecimal("14.51")

    assert(transformer.toValue("0000000000000014.51").get.equals(decimal))
  }

  test("LongTransformer") {
    val transformer = new LongTransformer

    val long : java.lang.Long = 431
    assert(transformer.toValue("431").get.equals(long))
  }

  test("Long failure"){
    val transformer = new LongTransformer
    assert(transformer.toValue("4sdsf31").equals(None))
  }



  test("NativeLongTransformer") {
    val transformer = new LongTransformer

    val long: Long = 432
    assert(long.equals(transformer.toValue("432").get))
  }


  test("Java BooleanTransformer") {
    val transformer = new JavaBooleanTransformer

    assert(true == transformer.toValue("true").get)

    assert(false == transformer.toValue("false").get)

    assert(None == transformer.toValue("blalbla"))

  }

  test("StringTransformer") {
    val transformer = new StringTransformer
    assert("hello".equals(transformer.toValue("hello").get))
  }


  test("JavaIntegerTransformer") {
    val transformer = new JavaIntegerTransformer
    assert(new java.lang.Integer(45).equals(transformer.toValue("0000000045").get))
  }

  test("java integer with invald data"){
    val transformer = new JavaIntegerTransformer
    assert(None.equals(transformer.toValue("00000sdfsdf00045")))
  }

  test("register singleton transformer"){
    TransformerRegistry.registerSingletonTransformer(classOf[MyTransformerBean], new MyTransformer)
    println(TransformerRegistry(classOf[MyTransformerBean]))
    assert(TransformerRegistry(classOf[MyTransformerBean]).get.getClass == classOf[MyTransformer])
  }


}

case class MyTransformerBean()

class MyTransformer extends StringValueTransformer[MyTransformerBean]{
  def toValue(from: String) = None
}