package com.recursivity.commons.validator

import org.scalatest.FunSuite

/**
 * @author Georgios Zapounidis
 */
class UrlValidatorTest extends FunSuite {
  var url = "http://github.com/wfaler/recursivity-commons"

  test("validate url") {
    val validator = Url("url", {url})
    assert(validator.isValid)
    url = "http://github.com"
    assert(validator.isValid)
    url = "http://www.github.com/wfaler/recursivity-commons?kv=v&h=l"
    assert(validator.isValid)
    url = "www.github.com/wfaler"
    assert(!validator.isValid)
  }
}
