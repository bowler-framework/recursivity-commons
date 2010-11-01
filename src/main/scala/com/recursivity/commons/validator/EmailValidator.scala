package com.recursivity.commons.validator

import java.util.regex.Pattern

/**
 * Created by IntelliJ IDEA.
 * User: wfaler
 * Date: Oct 30, 2010
 * Time: 5:52:53 PM
 * To change this template use File | Settings | File Templates.
 */

class EmailValidator(key: String, value: => String) extends Validator{
  def getKey = key

  def isValid: Boolean = {
    val p = Pattern.compile("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*((\\.[A-Za-z]{2,}){1}$)")
    val m = p.matcher({value})
    return m.matches
  }
}