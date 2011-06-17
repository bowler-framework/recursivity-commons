package com.recursivity.commons.validator

import org.scalatest.FunSuite

/**
 * User: tstevens
 * Date: 6/17/11
 */

class RegexStringValidatorTest extends FunSuite {

  val regex = "^passed$".r


  test("Regex passed"){
    assert(RegexString("regex", regex, "passed").isValid)
  }

  test("Regex failed"){
    assert(! RegexString("regex", regex, "fail").isValid)
  }

}