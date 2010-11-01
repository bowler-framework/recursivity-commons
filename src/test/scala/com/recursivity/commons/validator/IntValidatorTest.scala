package com.recursivity.commons.validator

import org.scalatest.FunSuite

/**
 * Created by IntelliJ IDEA.
 * User: wfaler
 * Date: Oct 31, 2010
 * Time: 3:54:04 PM
 * To change this template use File | Settings | File Templates.
 */

class IntValidatorTest extends FunSuite{
  var int = 5

  test("MinIntValidator Test"){
    int = 7
    val validator = new MinIntValidator("int", 6, {int})
    assert(validator.isValid)
    int = 5
    assert(!validator.isValid)
  }

  test("MaxIntValidator Test"){
    int = 5
    val validator = new MaxIntValidator("int", 6, {int})
    assert(validator.isValid)
    int = 7
    assert(!validator.isValid)
  }
  
}