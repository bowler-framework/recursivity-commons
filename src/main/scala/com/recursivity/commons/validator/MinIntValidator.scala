package com.recursivity.commons.validator

/**
 * Created by IntelliJ IDEA.
 * User: wfaler
 * Date: Oct 31, 2010
 * Time: 3:44:35 PM
 * To change this template use File | Settings | File Templates.
 */

case class MinIntValidator(key: String, min: Int, value: () => Int) extends Validator{
  def getKey = key

  def isValid = (min <= value())

  def getReplaceModel = List(("min", min))
}

object MinInt {
  def apply(key: String, min: Int, value: => Int) =
    MinIntValidator(key, min, () => value)
}
