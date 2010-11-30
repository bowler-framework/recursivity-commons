package com.recursivity.commons.bean

/**
 * Created by IntelliJ IDEA.
 * User: wfaler
 * Date: Apr 19, 2010
 * Time: 8:16:26 PM
 * To change this template use File | Settings | File Templates.
 */

class FloatTransformer extends StringValueTransformer{
  def toValue(from: String) = new java.lang.Float(from)

}