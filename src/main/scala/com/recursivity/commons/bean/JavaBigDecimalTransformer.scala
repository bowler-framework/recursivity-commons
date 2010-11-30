package com.recursivity.commons.bean

/**
 * Created by IntelliJ IDEA.
 * User: wfaler
 * Date: Mar 25, 2010
 * Time: 11:37:02 PM
 * To change this template use File | Settings | File Templates.
 */

class JavaBigDecimalTransformer extends StringValueTransformer{
  def toValue(from: String) = new java.math.BigDecimal(from)

}