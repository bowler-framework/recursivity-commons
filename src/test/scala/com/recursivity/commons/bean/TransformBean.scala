package com.recursivity.commons.bean

/**
 * Created by IntelliJ IDEA.
 * User: wfaler
 * Date: Mar 25, 2010
 * Time: 11:28:22 PM
 * To change this template use File | Settings | File Templates.
 */

class TransformBean{
  var hello: String = "hello"
  var int: Int = 0
  var long: Long = 0
  var javaLong: java.lang.Long = 0
  var date: java.util.Date = new java.util.Date
  var bool: Boolean = true
  var javaBool: java.lang.Boolean = true
  var  javaInt: java.lang.Integer = 56
  var javaBigDecimal: java.math.BigDecimal = new java.math.BigDecimal("45.34")
  var bigDecimal: BigDecimal = new BigDecimal(javaBigDecimal)
}