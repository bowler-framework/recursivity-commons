package com.recursivity.commons.validator

import org.scalatest.FunSuite
import com.recursivity.commons.DateHelper._
/**
 * Created by IntelliJ IDEA.
 * User: wfaler
 * Date: Nov 4, 2010
 * Time: 3:28:47 PM
 * To change this template use File | Settings | File Templates.
 */

class ClasspathMessageResolverTest extends FunSuite {
  test("Message resolution"){
    val bean = new MyBean("hello", 5, 2 days from_now, None)
    val minLen = MinLength("hello", 8, {bean.text})
    assert(!minLen.isValid)

    val resolver = new ClasspathMessageResolver(this.getClass)
    val result = resolver.resolveMessage(minLen)
    //println(result)
    assert("'your text' must be at least 8 characters long".equals(result))
  }

  test("localized"){
    val bean = new MyBean("hello", 5, 2 days from_now, None)
    val minLen = MinLength("hello", 8, {bean.text})
    assert(!minLen.isValid)

    val resolver = new ClasspathMessageResolver(this.getClass, List("se"))
    val result = resolver.resolveMessage(minLen)
    println(result)
    assert("'din text' maste vara minst 8 tecken lang".equals(result))

  }

  test("localized - no properties, fallback to default"){
    val bean = new MyBean("hello", 5, 2 days from_now, None)
    val minLen = MinLength("hello", 8, {bean.text})
    assert(!minLen.isValid)

    val resolver = new ClasspathMessageResolver(this.getClass, List("es"))
    val result = resolver.resolveMessage(minLen)
    //println(result)
    assert("'your text' must be at least 8 characters long".equals(result))
  }


  test("localized - no properties, fallback to second choice"){
    val bean = new MyBean("hello", 5, 2 days from_now, None)
    val minLen = MinLength("hello", 8, {bean.text})
    assert(!minLen.isValid)

    val resolver = new ClasspathMessageResolver(this.getClass, List("es", "se"))
    val result = resolver.resolveMessage(minLen)
    //println(result)
    assert("'din text' maste vara minst 8 tecken lang".equals(result))
  }


  test("localized with 2 choices - no properties, fallback to default"){
    val bean = new MyBean("hello", 5, 2 days from_now, None)
    val minLen = MinLength("hello", 8, {bean.text})
    assert(!minLen.isValid)

    val resolver = new ClasspathMessageResolver(this.getClass, List("es", "fi"))
    val result = resolver.resolveMessage(minLen)
    assert("'your text' must be at least 8 characters long".equals(result))
  }

  test("no property for key defined"){
    val bean = new MyBean("hello", 5, 2 days from_now, None)
    val minLen = MinLength("howdy", 8, {bean.text})
    assert(!minLen.isValid)

    val resolver = new ClasspathMessageResolver(this.getClass)
    val result = resolver.resolveMessage(minLen)
    //println(result)
    assert("howdy must be at least 8 characters long".equals(result))
  }
}
