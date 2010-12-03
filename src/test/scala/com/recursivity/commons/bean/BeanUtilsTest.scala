package com.recursivity.commons.bean

import org.scalatest.FunSuite

/**
 * Created by IntelliJ IDEA.
 * User: wfaler
 * Date: Nov 29, 2010
 * Time: 11:52:46 PM
 * To change this template use File | Settings | File Templates.
 */

class BeanUtilsTest extends FunSuite{

  test("instantiate case-class"){
    val bean = BeanUtils.instantiate(classOf[TestBean]).asInstanceOf[TestBean]
    assert(bean != null)
    assert(bean.isInstanceOf[TestBean])
    assert(bean.option == None)
  }


  test("set bean values"){
    val map = Map("nonexistent" -> "hello", "name" -> "wille", "long" -> "1", "int" -> "1","short" -> "2","double" -> "3","float" -> "4",
      "bool" -> "true","decimal" -> "3.14","option" -> "someOption","list" -> List("1", "2", "3").toArray)

    val bean = BeanUtils.instantiate(classOf[TestBean], map).asInstanceOf[TestBean]

    assert(bean.name == "wille")
    assert(bean.long == 1L)
    assert(bean.int == 1)
    assert(bean.short == new java.lang.Short("2"))
    assert(bean.double == 3.0D)
    assert(bean.float == 4.0f)
    assert(bean.decimal == new BigDecimal(new java.math.BigDecimal("3.14")))
    assert(bean.bool == true)

    //list option to test
   // assert(bean.name == "wille")
   // assert(bean.name == "wille")

    
  }

  test("List with primitive reflected generic type"){
    
  }

  test("Set"){

  }

  test("MutableList"){

  }

  test("LinkedList & other concrete mutables"){

  }

  test("ListSet & other concrete mutable"){
    
  }

  test("java.util.Set"){

  }

  test("java.util.List"){

  }

  test("concrete java.util.Set"){

  }

  test("concrete java.util.List"){
    
  }



}

class BaseBean(val name: String)

case class TestBean(value: String, long: Long, int: Int, short: Short, double: Double, float: Float,
                    bool: Boolean, b: Byte, char: Char, decimal: BigDecimal, option: Option[String], list: List[BigDecimal]) extends BaseBean(value)