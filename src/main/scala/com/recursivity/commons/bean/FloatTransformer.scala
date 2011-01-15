package com.recursivity.commons.bean

/**
 * Created by IntelliJ IDEA.
 * User: wfaler
 * Date: Apr 19, 2010
 * Time: 8:16:26 PM
 * To change this template use File | Settings | File Templates.
 */

class FloatTransformer extends StringValueTransformer[java.lang.Float]{
  def toValue(from: String): Option[java.lang.Float] = {
    try{
      return Some(new java.lang.Float(from))
    }catch{
      case e: Exception => return None
    }
  }
}