package com.recursivity.commons.validator

import collection.mutable.MutableList

/**
 * Created by IntelliJ IDEA.
 * User: wfaler
 * Date: Oct 30, 2010
 * Time: 5:53:25 PM
 * To change this template use File | Settings | File Templates.
 */

case class EqualsValidator(key: String, value1: () => Any, value2: () => Any) extends Validator{
  def getKey = key

  def isValid = (value1() == value2())

  def getReplaceModel = new MutableList[Tuple2[String, Any]].toList
}

object Equals {
  def apply(key: String, value1: => Any, value2: => Any) =
    EqualsValidator(key, () => value1, () => value2)
}
