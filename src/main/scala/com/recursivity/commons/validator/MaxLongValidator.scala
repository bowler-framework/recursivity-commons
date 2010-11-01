package com.recursivity.commons.validator

/**
 * Created by IntelliJ IDEA.
 * User: wfaler
 * Date: Oct 31, 2010
 * Time: 3:45:20 PM
 * To change this template use File | Settings | File Templates.
 */

class MaxLongValidator(key: String, max: Long, value: => Long) extends Validator{
  def getKey = key

  def isValid = (max >= value)
}