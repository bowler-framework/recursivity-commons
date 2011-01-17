package com.recursivity.commons.validator

import org.scalatest.FunSuite
import com.recursivity.commons.DateHelper._
import scala.{None}

/**
 * Created by IntelliJ IDEA.
 * User: wfaler
 * Date: Oct 30, 2010
 * Time: 5:26:23 PM
 * To change this template use File | Settings | File Templates.
 */

class NotNullOrNoneValidatorTest extends FunSuite {

  test("null"){
    val bean = new MyBean("hello", 1, 2 days from_now, Some(1))
    val validator = NotNullOrNone[String]("text", {bean.text})
    assert(validator.isValid)
    bean.text = null
    assert(!validator.isValid)
  }

  test("Some/None"){
    val bean = new MyBean("hello", 1, 2 days from_now, Some(1))
    val validator = NotNullOrNone[Option[Int]]("value", {bean.value})
    assert(validator.isValid)
    bean.value = None
    assert(!validator.isValid)
  }
}
