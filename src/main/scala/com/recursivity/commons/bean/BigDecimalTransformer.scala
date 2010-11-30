package com.recursivity.commons.bean

/**
 * Created by IntelliJ IDEA.
 * User: wfaler
 * Date: Mar 25, 2010
 * Time: 11:34:54 PM
 * To change this template use File | Settings | File Templates.
 */

class BigDecimalTransformer extends StringValueTransformer{
  def toValue(from: String) = new BigDecimal(new java.math.BigDecimal(from))

}