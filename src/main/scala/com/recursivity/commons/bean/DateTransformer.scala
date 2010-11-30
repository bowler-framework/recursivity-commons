package com.recursivity.commons.bean

import java.text.SimpleDateFormat

/**
 * Created by IntelliJ IDEA.
 * User: wfaler
 * Date: Mar 25, 2010
 * Time: 11:35:04 PM
 * To change this template use File | Settings | File Templates.
 */

class DateTransformer extends StringValueTransformer{
  def toValue(from: String): AnyRef = {
    val df = new SimpleDateFormat("yyyyyMMddHHmmssSSSZ")
    return df.parse(from)
  }

}