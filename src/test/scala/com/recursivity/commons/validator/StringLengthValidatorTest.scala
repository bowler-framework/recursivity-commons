package com.recursivity.commons.validator

import org.scalatest.FunSuite

/**
 * Created by IntelliJ IDEA.
 * User: wfaler
 * Date: Oct 31, 2010
 * Time: 2:46:45 PM
 * To change this template use File | Settings | File Templates.
 */

class StringLengthValidatorTest extends FunSuite{
  test("MinLength test"){
    val bean = new MyBean("hello", 1, new java.util.Date, Some(1))
    val validator = new MinLengthValidator(3, "text", {bean.text})
    assert(validator.isValid)
    bean.text = "12"
    assert(!validator.isValid)

  }

  test("MaxLength test"){
    val bean = new MyBean("hello", 1, new java.util.Date, Some(1))
    val validator = new MaxLengthValidator(3, "text", {bean.text})
    assert(!validator.isValid)
    bean.text = "12"
    assert(validator.isValid)
  }

  test("Length range test"){
    val bean = new MyBean("hello", 1, new java.util.Date, Some(1))
    val validator = new StringLengthValidator(3, 5, "text", {bean.text})
    assert(validator.isValid)
    bean.text = "12"
    assert(!validator.isValid)
    bean.text = "123456"
    assert(!validator.isValid)
  }


}