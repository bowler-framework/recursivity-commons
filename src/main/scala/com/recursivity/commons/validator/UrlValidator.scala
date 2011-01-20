package com.recursivity.commons.validator

import scala.collection.mutable.MutableList
import java.util.regex.Pattern

/**
 * Checks if an Url is well formed.
 *
 * @author Georgios Zapounidis
 */
case class UrlValidator(key: String, value: () => String) extends Validator {
  def getKey = key

  def isValid: Boolean = {
    val p = Pattern.compile("^(([^:/?#]+):)?(//([^/?#]*))?([^?#]*)(\\?([^#]*))?(#(.*))?")
    val m = p.matcher(value())
    m.matches
  }

  def getReplaceModel = new MutableList[Tuple2[String, Any]].toList
}

object Url {
  def apply(key: String, value: => String) = UrlValidator(key, () => value)
}
