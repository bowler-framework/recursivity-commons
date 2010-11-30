package com.recursivity.commons.bean

import org.scalatest.FunSuite

/**
 * Created by IntelliJ IDEA.
 * User: wfaler
 * Date: Nov 29, 2010
 * Time: 11:52:46 PM
 * To change this template use File | Settings | File Templates.
 */

class DynamicInstantiatorTest extends FunSuite{

  test("instantiate case-class"){
    val bean = DynamicInstantiator.instantiate(classOf[TestBean]).asInstanceOf[TestBean]
    assert(bean != null)
    assert(bean.isInstanceOf[TestBean])
    assert(bean.option == None)
  }
}

case class TestBean(name: String, long: Long, int: Int, short: Short, double: Double, float: Float,
                    bool: Boolean, b: Byte, char: Char, decimal: BigDecimal, option: Option[String], list: List[String])