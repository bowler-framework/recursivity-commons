package com.recursivity.commons

import org.scalatest.FunSuite
import java.io.InputStream

/**
 * Created by IntelliJ IDEA.
 * User: wfaler
 * Date: Nov 6, 2010
 * Time: 12:13:04 AM
 * To change this template use File | Settings | File Templates.
 */

class ClasspathTextResourceTest extends FunSuite{
  
  test("resolve default"){
    val res = new ClasspathTextResource(this.getClass, ".txt")
    assert("Howdy partner!" == res.load)
  }

  test("resolve internationalised"){
    val res = new ClasspathTextResource(this.getClass, ".txt", "se")
    assert("Hej pa dig!" == res.load)
  }

  test("resolve internationalised, fallback to default"){
    val res = new ClasspathTextResource(this.getClass, ".txt", "es")
    assert("Howdy partner!" == res.load)
  }

  test("test double close"){
    ClasspathResourceResolver.resolveResource(this.getClass, ".txt"){
      is => close(is)
    }
  }

  def close(is: InputStream){
    is.close
  }
}