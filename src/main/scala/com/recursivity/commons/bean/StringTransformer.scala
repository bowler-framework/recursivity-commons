package com.recursivity.commons.bean

import java.lang.String

/**
 * Created by IntelliJ IDEA.
 * User: wfaler
 * Date: Mar 25, 2010
 * Time: 11:34:18 PM
 * To change this template use File | Settings | File Templates.
 */

class StringTransformer extends StringValueTransformer[String]{
  def toValue(from: String) = Some(from)
}