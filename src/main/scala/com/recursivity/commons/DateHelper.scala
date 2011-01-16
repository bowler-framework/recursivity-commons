package com.recursivity.commons

import java.util.{Date, Calendar}

/**
 * implicits for dates, counting days ago and from now.
 */


class DateHelper(number: Int) {
  def days(when: String): Date = {
    var date = Calendar.getInstance()
    when match {
      case DateHelper.ago => date.add(Calendar.DAY_OF_MONTH, -number)
      case DateHelper.from_now => date.add(Calendar.DAY_OF_MONTH, number)
      case _ => date
    }
    date.getTime()
  }
}


object DateHelper {
  val ago = "ago"
  val from_now = "from_now"

  implicit def convertInt2DateHelper(number: Int) = new DateHelper(number)

}