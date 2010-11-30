package com.recursivity.commons.bean

/**
 * Created by IntelliJ IDEA.
 * User: wfaler
 * Date: Mar 25, 2010
 * Time: 11:36:46 PM
 * To change this template use File | Settings | File Templates.
 */

class JavaBooleanTransformer extends StringValueTransformer{
def toValue(from: String): AnyRef = {
    if(from.equals("true"))
      return new java.lang.Boolean("true")
    else if(from.equals("false"))
      return new java.lang.Boolean("false")
    else throw new IllegalArgumentException("not true or false")
  }

}