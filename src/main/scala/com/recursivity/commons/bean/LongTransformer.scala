package com.recursivity.commons.bean

import java.lang.String

/**
 * Created by IntelliJ IDEA.
 * User: wfaler
 * Date: Mar 25, 2010
 * Time: 11:35:39 PM
 * To change this template use File | Settings | File Templates.
 */

class LongTransformer extends StringValueTransformer{
  def toValue(stored: String) = new java.lang.Long(stored)


}