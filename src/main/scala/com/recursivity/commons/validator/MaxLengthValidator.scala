package com.recursivity.commons.validator

/**
 * Created by IntelliJ IDEA.
 * User: wfaler
 * Date: Oct 30, 2010
 * Time: 5:58:55 PM
 * To change this template use File | Settings | File Templates.
 */

case class MaxLengthValidator(key: String, maxLength: Int, value: () => String) extends Validator{
  def getKey = key

  def isValid: Boolean = {
    val string = value()
    if (string != null && string.length <= maxLength) true else false
  }

  def getReplaceModel = List(("max", maxLength))
}

object MaxLength {
  def apply(key: String, length: Int, value: => String) =
    MaxLengthValidator(key, length, () => value)
}
