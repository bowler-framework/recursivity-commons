package com.recursivity.commons.validator

import util.matching.Regex


case class RegexStringValidator(key: String, regex: Regex, value: () => String) extends Validator {
  lazy val getKey = key

  def isValid = {
    val str = value()

    str != null && regex.findFirstIn(str).isDefined
  }

  lazy val getReplaceModel = List(("regex", regex.toString))
}

object RegexString {
  def apply(key: String, regex: Regex, value: => String) = RegexStringValidator(key, regex, () => value)
}