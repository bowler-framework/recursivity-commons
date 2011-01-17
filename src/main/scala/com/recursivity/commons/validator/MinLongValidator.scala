package com.recursivity.commons.validator

/**
 * Created by IntelliJ IDEA.
 * User: wfaler
 * Date: Oct 31, 2010
 * Time: 3:44:44 PM
 * To change this template use File | Settings | File Templates.
 */

case class MinLongValidator(key: String, min: Long, value: () => Long) extends Validator{
  def getKey = key

  def isValid = (min <= value())

  def getReplaceModel = List(("min", min))
}

object MinLong {
  def apply(key: String, min: Long, value: => Long) =
    MinLongValidator(key, min, () => value)
}
