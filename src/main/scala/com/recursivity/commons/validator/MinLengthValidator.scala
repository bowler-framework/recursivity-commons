package com.recursivity.commons.validator

/**
 * Created by IntelliJ IDEA.
 * User: wfaler
 * Date: Oct 30, 2010
 * Time: 5:58:48 PM
 * To change this template use File | Settings | File Templates.
 */

class MinLengthValidator(key: String, minLength: Int, value: => String) extends Validator{
  def getKey = key

  def isValid: Boolean = {
    val string = value
    if(string != null &&  string.length >= minLength) return true;
    else return false;
  }

  def getReplaceModel = List(("min", minLength))
}