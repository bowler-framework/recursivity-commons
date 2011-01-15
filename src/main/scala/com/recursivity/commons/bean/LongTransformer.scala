package com.recursivity.commons.bean

import java.lang.String

/**
 * Created by IntelliJ IDEA.
 * User: wfaler
 * Date: Mar 25, 2010
 * Time: 11:35:39 PM
 * To change this template use File | Settings | File Templates.
 */

class LongTransformer extends StringValueTransformer[Long]{
  def toValue(from: String): Option[Long] = {
    try{
      return Some(new java.lang.Long(from).longValue)
    }catch{
      case e: Exception => return None
    }

  }


}