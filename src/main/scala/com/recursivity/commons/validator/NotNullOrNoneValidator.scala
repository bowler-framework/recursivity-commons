package com.recursivity.commons.validator

import collection.mutable.MutableList

/**
 * Created by IntelliJ IDEA.
 * User: wfaler
 * Date: Oct 30, 2010
 * Time: 5:17:47 PM
 * To change this template use File | Settings | File Templates.
 */

case class NotNullOrNoneValidator[T](key: String, value: () => T) extends Validator {
  def isValid: Boolean = {
    val validatable = value()
    println("validatable = "+ validatable == null)

    if (validatable == null) return false
    if (validatable.isInstanceOf[Option[T]]) {
      if (validatable.isInstanceOf[Some[T]]) return true else return false
    }
    true
  }

  def getKey = key

  def getReplaceModel = new MutableList[Tuple2[String, Any]].toList
}

object NotNullOrNone {
  def apply[T](key: String, value: => T) =
    NotNullOrNoneValidator(key, () => value)
}
