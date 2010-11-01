package com.recursivity.commons.validator

/**
 * Created by IntelliJ IDEA.
 * User: wfaler
 * Date: Oct 30, 2010
 * Time: 5:53:25 PM
 * To change this template use File | Settings | File Templates.
 */

class EqualsValidator(key: String, value1: => Any, value2: => Any) extends Validator{
  def getKey = key

  def isValid = (value1 == value2)
}