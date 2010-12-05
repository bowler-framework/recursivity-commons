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
      "bool" -> "true","decimal" -> "3.14","option" -> "someOption","list" -> List("1.12", "2.1", "3.14").toArray)

    val bean = BeanUtils.instantiate(classOf[TestBean], map).asInstanceOf[TestBean]

    assert(bean.name == "wille")
    assert(bean.long == 1L)
    assert(bean.int == 1)
    assert(bean.short == new java.lang.Short("2"))
    assert(bean.double == 3.0D)
    assert(bean.float == 4.0f)
    assert(bean.decimal == new BigDecimal(new java.math.BigDecimal("3.14")))
    assert(bean.bool == true)

    assert(bean.list(0).equals(new BigDecimal(new java.math.BigDecimal("1.12"))))
    assert(bean.list(1).equals(new BigDecimal(new java.math.BigDecimal("2.1"))))
    assert(bean.list(2).equals(new BigDecimal(new java.math.BigDecimal("3.14"))))
    
  }

  test("option with list"){
    val map = Map("list" -> List("1", "2", "3", "4", "5"))
    val bean = BeanUtils.instantiate(classOf[NestedGenerics], map).asInstanceOf[NestedGenerics]
    assert(bean != null)
    val list = bean.list.get
    assert(list != null)
    println(list)
    assert(list.size == 5)
    assert(list(0) == 1)
    assert(list(1) == 2)
    assert(list(2) == 3)
    assert(list(3) == 4)
    assert(list(4) == 5)
  }

  test("Option type with primitive reflected generic type"){
    val map = Map("num" -> "true")
    val bean = BeanUtils.instantiate(classOf[PrimitiveOption], map).asInstanceOf[PrimitiveOption]
    assert(bean != null)
    assert(bean.num.equals(Some(true)))
  }

  test("Set"){
    val map = Map("set" -> List("1", "2", "3", "4", "4"))
    val bean = BeanUtils.instantiate(classOf[SetBean], map).asInstanceOf[SetBean]
    assert(bean != null)
    assert(bean.set.size == 4)
    assert(bean.set.foldLeft(0)((b,a) => b + a) == 10)
  }

  test("Array"){
    
  }

  test("MutableList"){
    val set = Set(1, 2, 3, 4)

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

case class SetBean(set: Set[Int])

case class PrimitiveOption(num: Option[Boolean])

case class NestedGenerics(list: Option[List[Int]])

class BaseBean(val name: String)

case class TestBean(value: String, long: Long, int: Int, short: Short, double: Double, float: Float,
                    bool: Boolean, b: Byte, char: Char, decimal: BigDecimal, option: Option[String], list: List[BigDecimal]) extends BaseBean(value)