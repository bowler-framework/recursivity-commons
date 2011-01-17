package com.recursivity.commons.validator

import org.scalatest.FunSuite

/**
 * Created by IntelliJ IDEA.
 * User: wfaler
 * Date: Oct 31, 2010
 * Time: 3:54:38 PM
 * To change this template use File | Settings | File Templates.
 */

class EmailValidatorTest extends FunSuite{
  var email = "wille.faler@gmail.com"

  test("validate email"){
    val validator = Email("email", {email})
    assert(validator.isValid)
    email = "wille@gdg dfg.se"
    assert(!validator.isValid)
    email = "willeAtdfgh.se"
    assert(!validator.isValid)
    email = "willeA@tdfgh."
    assert(!validator.isValid)
    email = "wille.faler@gmail.gmail.se"
    assert(validator.isValid)
    email = "wille_faler@gm-ail.com"
    assert(validator.isValid)
    email = "wille faler@gmail.com"
    assert(!validator.isValid)
  }
}
