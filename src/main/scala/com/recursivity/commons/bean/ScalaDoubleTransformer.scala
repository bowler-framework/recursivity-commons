package com.recursivity.commons.bean

/**
 * Created by IntelliJ IDEA.
 * User: wfaler
 * Date: Apr 19, 2010
 * Time: 8:17:09 PM
 * To change this template use File | Settings | File Templates.
 */

class ScalaDoubleTransformer extends StringValueTransformer[Double]{
  def toValue(from: String): Option[Double] = {
    try{
      return Some(from.toDouble)
    }catch{
      case e: Exception => return None
    }
  }

}