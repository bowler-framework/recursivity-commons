package com.recursivity.commons.validator

/**
 * Created by IntelliJ IDEA.
 * User: wfaler
 * Date: Oct 31, 2010
 * Time: 3:45:20 PM
 * To change this template use File | Settings | File Templates.
 */

case class MaxLongValidator(key: String, max: Long, value: () => Long) extends Validator{
  def getKey = key

  def isValid = (max >= value())


  def getReplaceModel = List(("max", max))
}

object MaxLong {
  def apply(key: String, max: Long, value: => Long) =
    MaxLongValidator(key, max, () => value)
}
