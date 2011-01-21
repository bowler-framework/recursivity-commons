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
    url = "http://github.com/"
    assert(validator.isValid)

    url = "http://localhost"
    assert(validator.isValid)

    url = "http://localhost/"
    assert(validator.isValid)

    url = "http://localhost:8080"
    assert(validator.isValid)

    url = "http://localhost/test1"
    assert(validator.isValid)

    url = "http://localhost/test1/"
    assert(validator.isValid)

    url = "http://localhost?action=view"
    assert(validator.isValid)

    url = "http://localhost/test1?action=view"
    assert(validator.isValid)

    url = "http://tech.yahoo.com/rc/desktops/102;_ylt=Ao8yevQHlZ4On0O3ZJGXLEQFLZA5"
    assert(validator.isValid)

    url = "http://user@host:80/path"
    assert(validator.isValid)

    url = "http://user:password@host:80/path"
    assert(validator.isValid)


    url = "http://www.github.com/wfaler/recursivity-commons?kv=v&h=l"
    assert(validator.isValid)
    url = "http://www.github.com/wfaler/recursivity    -commons?kv=v&h=l"
    assert(!validator.isValid)
    url = "www.github.com/wfaler"
    assert(!validator.isValid)
  }
}
