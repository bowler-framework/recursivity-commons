package com.recursivity.commons.validator

import org.scalatest.FunSuite
import com.recursivity.commons.DateHelper._
import java.util.Date

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
    group.add(MinLength("hello", 3, {bean.text}))
    group.add(MaxInt("max", 6, {bean.number}))
    group.add(MinInt("min", 4, {bean.number}))
    group.add(NotNullOrNone("null", {bean.text}))

    val failures = group.validateAndReturnFailures

    assert(failures.length == 0)
  }

  test("failedValidation"){
    val bean = new MyBean("hello", 5, 2 days from_now, None)
    val group = new ValidationGroup
    val minLen = MinLength("hello", 8, {bean.text})
    group add minLen
    val max = MaxInt("max", 3, {bean.number})
    group add max
    val min = MinInt("min", 6, {bean.number})
    group add min
    val notNull = NotNullOrNone("null", {bean.value})
    group add notNull
    

    val failures = group.validateAndReturnFailures

    assert(failures.length == 4)

    assert(failures.contains(minLen))
    assert(failures.contains(min))
    assert(failures.contains(max))
    assert(failures.contains(notNull))
  }

  test("validation messages"){
    // setup a bean to validate
    val bean = new MyBean("hello", 5, new Date, None)

    // new ValidationGroup with a ClassPathMessageResolver
    val group = ValidationGroup(new ClasspathMessageResolver(this.getClass))

    // add the validators to the ValidationGroup
    group.add(MinLength("hello", 8, {bean.text}))
    group.add(MaxInt("max", 3, {bean.number}))
    group.add(MinInt("min", 6, {bean.number}))
    group.add(NotNullOrNone("null", {bean.value}))

    // validate and return error messages, a List of Tuple2's with (key, errorMessage) format.
    val failures = group.validateAndReturnErrorMessages

    failures.foreach(f =>{
      if(f._1.equals("hello")){
        assert(f._2.equals("Howdy must be at least 8 characters long"))
      }else if(f._1.equals("max")){
        assert(f._2.equals("MaxInt must be < 3"))
      }else if(f._1.equals("min")){
        println(f._2)
        assert(f._2.equals("MinInt must be > 6"))
      }else if(f._1.equals("null")){
        assert(f._2.equals("'NotNull Value' cannot be null or empty"))
      }else
        fail
    })
    
  }


}
