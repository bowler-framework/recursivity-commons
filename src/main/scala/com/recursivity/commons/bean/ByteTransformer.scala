package com.recursivity.commons.bean

/**
 * Created by IntelliJ IDEA.
 * User: wfaler
 * Date: Dec 2, 2010
 * Time: 10:56:53 PM
 * To change this template use File | Settings | File Templates.
 */

class ByteTransformer extends StringValueTransformer[java.lang.Byte]{
  def toValue(from: String): Option[java.lang.Byte] = {
    try{
      return Some(new java.lang.Byte(from))
    }catch{
      case e: Exception => return None
    }

  }

}