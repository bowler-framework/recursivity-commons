package com.recursivity.commons.validator

import collection.mutable.MutableList

/**
 * Created by IntelliJ IDEA.
 * User: wfaler
 * Date: Oct 30, 2010
 * Time: 5:17:47 PM
 * To change this template use File | Settings | File Templates.
 */

class NotNullOrNoneValidator[T](key: String, value: => T) extends Validator{
  def isValid: Boolean = {
    val validatable = value
    if(validatable == null)
      return false
    if(validatable.isInstanceOf[Option[T]]){
      if(validatable.isInstanceOf[Some[T]])
        return true
      else
        return false
    }
    return true
  }

  def getKey = key

  def getReplaceModel = new MutableList[Tuple2[String, Any]].toList
}