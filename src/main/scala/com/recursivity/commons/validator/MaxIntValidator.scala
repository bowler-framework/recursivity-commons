package com.recursivity.commons.validator

/**
 * Created by IntelliJ IDEA.
 * User: wfaler
 * Date: Oct 31, 2010
 * Time: 3:45:12 PM
 * To change this template use File | Settings | File Templates.
 */

class MaxIntValidator(key: String, max: Int, value: => Int) extends Validator{
  def getKey = key

  def isValid = (max >= value)

  def getReplaceModel = List(("max", max))
}