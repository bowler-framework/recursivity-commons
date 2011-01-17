package com.recursivity.commons.validator

import org.scalatest.FunSuite

/**
 * Created by IntelliJ IDEA.
 * User: wfaler
 * Date: Oct 31, 2010
 * Time: 3:54:48 PM
 * To change this template use File | Settings | File Templates.
 */

class EqualsValidatorTest extends FunSuite{
  var value1 = "hello"
  var value2 = "hello2"

  test("test equals"){
    val validator = Equals("equals", {value1}, {value2})
    assert(!validator.isValid)
    value2 = "hello"
    assert(validator.isValid)
  }
}
