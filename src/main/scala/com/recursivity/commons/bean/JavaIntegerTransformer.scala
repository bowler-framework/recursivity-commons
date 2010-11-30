package com.recursivity.commons.bean

/**
 * Created by IntelliJ IDEA.
 * User: wfaler
 * Date: Mar 25, 2010
 * Time: 11:34:41 PM
 * To change this template use File | Settings | File Templates.
 */

class JavaIntegerTransformer extends StringValueTransformer{
  def toValue(from: String): AnyRef = {
    return new java.lang.Integer(Integer.parseInt(from))
  }


}