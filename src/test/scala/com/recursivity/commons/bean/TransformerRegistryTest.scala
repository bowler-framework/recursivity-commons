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
    assert(classOf[BigDecimalTransformer].equals(TransformerRegistry.resolveTransformer(classOf[BigDecimal]).get.getClass))
    assert(classOf[JavaBooleanTransformer].equals(TransformerRegistry.resolveTransformer(classOf[Boolean]).get.getClass))
    assert(classOf[DateTransformer].equals(TransformerRegistry.resolveTransformer(classOf[java.util.Date]).get.getClass))
    assert(classOf[JavaBigDecimalTransformer].equals(TransformerRegistry.resolveTransformer(classOf[java.math.BigDecimal]).get.getClass))
    assert(classOf[JavaBooleanTransformer].equals(TransformerRegistry.resolveTransformer(classOf[java.lang.Boolean]).get.getClass))
    assert(classOf[JavaIntegerTransformer].equals(TransformerRegistry.resolveTransformer(classOf[java.lang.Integer]).get.getClass))
    assert(classOf[LongTransformer].equals(TransformerRegistry.resolveTransformer(classOf[java.lang.Long]).get.getClass))
    assert(classOf[LongTransformer].equals(TransformerRegistry.resolveTransformer(classOf[Long]).get.getClass))
    assert(classOf[StringTransformer].equals(TransformerRegistry.resolveTransformer(classOf[String]).get.getClass))

  }

  test("BigDecimalTransformer") {
    val transformer = new BigDecimalTransformer
    val decimal = new BigDecimal(new java.math.BigDecimal("14.51"))
    assert(transformer.toValue("0000000000000014.51").equals(decimal))

  }

  test("JavaBigDecimalTransformer") {
    val transformer = new JavaBigDecimalTransformer

    val decimal = new java.math.BigDecimal("14.51")

    assert(transformer.toValue("0000000000000014.51").equals(decimal))
  }

  test("LongTransformer") {
    val transformer = new LongTransformer

    val long : java.lang.Long = 431
    assert(transformer.toValue("431").equals(long))
  }
  test("NativeLongTransformer") {
    val transformer = new LongTransformer

    val long: Long = 432
    assert(long.equals(transformer.toValue("432")))
  }


  test("Java BooleanTransformer") {
    val transformer = new JavaBooleanTransformer

    assert(true == transformer.toValue("true"))

    assert(false == transformer.toValue("false"))

  }

  test("StringTransformer") {
    val transformer = new StringTransformer
    assert("hello".equals(transformer.toValue("hello")))
  }


  test("JavaIntegerTransformer") {
    val transformer = new JavaIntegerTransformer
    assert(new java.lang.Integer(45).equals(transformer.toValue("0000000045")))
  }


}