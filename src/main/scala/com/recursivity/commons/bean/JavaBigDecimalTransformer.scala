package com.recursivity.commons.bean

/**
 * Created by IntelliJ IDEA.
 * User: wfaler
 * Date: Mar 25, 2010
 * Time: 11:37:02 PM
 * To change this template use File | Settings | File Templates.
 */

class JavaBigDecimalTransformer extends StringValueTransformer[java.math.BigDecimal]{
  def toValue(from: String): Option[java.math.BigDecimal] = {
    try{
      return Some(new java.math.BigDecimal(from))
    }catch{
      case e: Exception => return None
    }

  }

}