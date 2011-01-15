package com.recursivity.commons.bean

/**
 * Created by IntelliJ IDEA.
 * User: wfaler
 * Date: Dec 2, 2010
 * Time: 10:56:53 PM
 * To change this template use File | Settings | File Templates.
 */

class ShortTransformer extends StringValueTransformer[java.lang.Short]{
  def toValue(from: String): Option[java.lang.Short] = {
    try{
      return Some(new java.lang.Short(from))
    }catch{
      case e: Exception => return None
    }

  }

}