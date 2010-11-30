package com.recursivity.commons.bean

/**
 * Created by IntelliJ IDEA.
 * User: wfaler
 * Date: Apr 19, 2010
 * Time: 8:17:09 PM
 * To change this template use File | Settings | File Templates.
 */

class DoubleTransformer extends StringValueTransformer{
  def toValue(from: String) = new java.lang.Double(from)

}