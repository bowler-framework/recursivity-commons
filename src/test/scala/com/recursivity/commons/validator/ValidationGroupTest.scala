package com.recursivity.commons.validator

import org.scalatest.FunSuite
import com.recursivity.commons.DateHelper._
/**
 * Created by IntelliJ IDEA.
 * User: wfaler
 * Date: Oct 31, 2010
 * Time: 3:54:55 PM
 * To change this template use File | Settings | File Templates.
 */

class ValidationGroupTest extends FunSuite{


  test("successful validation"){
    val bean = new MyBean("hello", 5, 2 days from_now, Some(2))
    val group = new ValidationGroup
    group.add(new MinLengthValidator("hello", 3, {bean.text}))
    group.add(new MaxIntValidator("max", 6, {bean.number}))
    group.add(new MinIntValidator("min", 4, {bean.number}))
    group.add(new NotNullOrNoneValidator("null", {bean.text}))

    val failures = group.validateAndReturnFailures

    assert(failures.length == 0)
  }

  test("failedValidation"){
    val bean = new MyBean("hello", 5, 2 days from_now, None)
    val group = new ValidationGroup
    val minLen = new MinLengthValidator("hello", 8, {bean.text})
    group add minLen
    val max = new MaxIntValidator("max", 3, {bean.number})
    group add max
    val min = new MinIntValidator("min", 6, {bean.number})
    group add min
    val notNull = new NotNullOrNoneValidator("null", {bean.value})
    group add notNull

    val failures = group.validateAndReturnFailures

    assert(failures.length == 4)

    assert(failures.contains(minLen))
    assert(failures.contains(min))
    assert(failures.contains(max))
    assert(failures.contains(notNull))
  }
}