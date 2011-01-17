package com.recursivity.commons.bean

/**
 * Created by IntelliJ IDEA.
 * User: wfaler
 * Date: Mar 25, 2010
 * Time: 11:36:46 PM
 * To change this template use File | Settings | File Templates.
 */

class JavaBooleanTransformer extends StringValueTransformer[java.lang.Boolean]{
def toValue(from: String): Option[java.lang.Boolean] = {
    if(from == null)
      return None
    if(from.equals("true"))
      return Some(new java.lang.Boolean("true"))
    else if(from.equals("false"))
      return Some(new java.lang.Boolean("false"))
    else return None
  }

}