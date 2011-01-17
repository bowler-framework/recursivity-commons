package com.recursivity.commons.validator

import java.util.regex.Pattern
import collection.mutable.MutableList

/**
 * Created by IntelliJ IDEA.
 * User: wfaler
 * Date: Oct 30, 2010
 * Time: 5:52:53 PM
 * To change this template use File | Settings | File Templates.
 */

case class EmailValidator(key: String, value: () => String) extends Validator {
  def getKey = key

  def isValid: Boolean = {
    val p = Pattern.compile("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*((\\.[A-Za-z]{2,}){1}$)")
    val m = p.matcher(value())
    m.matches
  }

  def getReplaceModel = new MutableList[Tuple2[String, Any]].toList
}

object Email {
  def apply(key: String, value: => String) = EmailValidator(key, () => value)
}
