package com.recursivity.commons.bean

import org.scalatest.FunSuite
import collection.immutable.ListSet
import collection.mutable.{LinkedHashSet, LinkedList, MutableList}

/**
 * Created by IntelliJ IDEA.
 * User: wfaler
 * Date: Nov 29, 2010
 * Time: 11:52:46 PM
 * To change this template use File | Settings | File Templates.
 */

class BeanUtilsTest extends FunSuite{

  test("instantiate case-class"){
    val bean = BeanUtils.instantiate[TestBean](classOf[TestBean])
    assert(bean != null)
    assert(bean.isInstanceOf[TestBean])
    assert(bean.option == None)
  }


  test("set bean values"){
    val map = Map("nonexistent" -> "hello", "name" -> "wille", "long" -> "1", "int" -> "1","short" -> "2","double" -> "3","float" -> "4",
      "bool" -> "true","decimal" -> "3.14","option" -> "someOption","list" -> List("1.12", "2.1", "3.14").toArray)

    val bean = BeanUtils.instantiate[TestBean](classOf[TestBean], map)

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
    val bean = BeanUtils.instantiate[NestedGenerics](classOf[NestedGenerics], map)
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
    val bean = BeanUtils.instantiate[PrimitiveOption](classOf[PrimitiveOption], map)
    assert(bean != null)
    assert(bean.num.equals(Some(true)))
  }

  test("Set"){
    val map = Map("set" -> List("1", "2", "3", "4", "4"))
    val bean = BeanUtils.instantiate[SetBean](classOf[SetBean], map)
    assert(bean != null)
    assert(bean.set.size == 4)
    assert(bean.set.foldLeft(0)((b,a) => b + a) == 10)
  }

  test("MutableList"){
    val map = Map("list" -> List("1", "2", "3", "4", "4"))
    val bean = BeanUtils.instantiate[MutableListBean](classOf[MutableListBean], map)
    assert(bean != null)
    assert(bean.list.size == 5)
    assert(bean.list.foldLeft(0)((b,a) => b + a) == 14)

  }

  test("Growable mutables"){
    val map = Map("list" -> List("1", "2", "3", "4", "4"))
    val bean = BeanUtils.instantiate[GrowableSetOrListBean](classOf[GrowableSetOrListBean], map)
    assert(bean != null)
    assert(bean.list.size == 4)
    assert(bean.list.foldLeft(0)((b,a) => b + a) == 10)
  }

  test("mutable LinkedList (not a Growable)"){
    val map = Map("list" -> List("1", "2", "3", "4", "4"))
    val bean = BeanUtils.instantiate[LinkedListBean](classOf[LinkedListBean], map)
    assert(bean != null)
    assert(bean.list.size == 5)
    assert(bean.list.foldLeft(0)((b,a) => b + a) == 14)

  }

  test("ListSet"){
    val map = Map("list" -> List("1", "2", "3", "4", "4"))
    val bean = BeanUtils.instantiate[ListSetBean](classOf[ListSetBean], map)
    assert(bean != null)
    assert(bean.list.size == 4)
    assert(bean.list.foldLeft(0)((b,a) => b + a) == 10)
    
  }

  test("java.util.Set"){
    val map = Map("set" -> List("1", "2", "3", "4", "4"))
    val bean = BeanUtils.instantiate[JavaSet](classOf[JavaSet], map)
    assert(bean != null)
    assert(bean.set.size == 4)
    var i = 0
    val iter = bean.set.iterator
    while(iter.hasNext)
      i = i + iter.next
    assert(10 == i)
  }

  test("java.util.List"){
    val map = Map("list" -> List("1", "2", "3", "4", "4"))
    val bean = BeanUtils.instantiate[JavaList](classOf[JavaList], map)
    assert(bean != null)
    assert(bean.list.size == 5)
    var i = 0
    val iter = bean.list.iterator
    while(iter.hasNext)
      i = i + iter.next
    assert(14 == i)
  }

  test("concrete java.util.Set"){
    val map = Map("set" -> List("1", "2", "3", "4", "4"))
    val bean = BeanUtils.instantiate[JavaTreeSet](classOf[JavaTreeSet], map)
    assert(bean != null)
    assert(bean.set.size == 4)
    var i = 0
    val iter = bean.set.iterator
    while(iter.hasNext)
      i = i + iter.next
    assert(10 == i)
  }

  test("concrete java.util.List"){
    val map = Map("list" -> List("1", "2", "3", "4", "4"))
    val bean = BeanUtils.instantiate[JavaLinkedList](classOf[JavaLinkedList], map)
    assert(bean != null)
    assert(bean.list.size == 5)
    var i = 0
    val iter = bean.list.iterator
    while(iter.hasNext)
      i = i + iter.next
    assert(14 == i)
  }

  test("Seq"){
    val map = Map("seq" -> List("1", "2", "3", "4", "4"))
    val bean = BeanUtils.instantiate[SeqBean](classOf[SeqBean], map)
    assert(bean != null)
    assert(bean.seq.size == 5)
    assert(bean.seq.foldLeft(0)((b,a) => b + a) == 14)
  }

  test("IllegalArgumentTest"){
    val map = Map("int" -> "r", "long" -> "5", "string" -> "hello")
    val bean = BeanUtils.instantiate[IntPrimitiveBean](classOf[IntPrimitiveBean], map)
    println(bean)

  }

}

case class IntPrimitiveBean(int: Int, long: Long, string: String)

case class SeqBean(seq: Seq[Int])

case class JavaLinkedList(list: java.util.LinkedList[Int])

case class JavaList(list: java.util.List[Int])

case class JavaSet(set: java.util.Set[Int])

case class JavaTreeSet(set: java.util.TreeSet[Int])

case class LinkedListBean(list: LinkedList[Int])

case class GrowableSetOrListBean(list: LinkedHashSet[Int])

case class ListSetBean(list: ListSet[Int])


case class MutableListBean(list: MutableList[Int])

case class SetBean(set: Set[Int])

case class PrimitiveOption(num: Option[Boolean])

case class NestedGenerics(list: Option[List[Int]])

class BaseBean(val name: String)

case class TestBean(value: String, long: Long, int: Int, short: Short, double: Double, float: Float,
                    bool: Boolean, b: Byte, char: Char, decimal: BigDecimal, option: Option[String], list: List[BigDecimal]) extends BaseBean(value)