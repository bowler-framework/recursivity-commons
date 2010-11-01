package com.recursivity.commons.validator

import org.scalatest.FunSuite

/**
 * Created by IntelliJ IDEA.
 * User: wfaler
 * Date: Oct 31, 2010
 * Time: 3:54:10 PM
 * To change this template use File | Settings | File Templates.
 */

class LongValidatorTest extends FunSuite{
  var long: Long = 5
  var limit: Long = 6

  test("MinIntValidator Test"){
    long = 7
    val validator = new MinLongValidator("int", limit, {long})
    assert(validator.isValid)
    long = 5
    assert(!validator.isValid)
  }

  test("MaxIntValidator Test"){
    long = 5
    val validator = new MaxLongValidator("int", limit, {long})
    assert(validator.isValid)
    long = 7
    assert(!validator.isValid)
  }

}