package com.recursivity.commons.bean

/**
 * Created by IntelliJ IDEA.
 * User: wfaler
 * Date: Mar 25, 2010
 * Time: 11:23:18 PM
 * To change this template use File | Settings | File Templates.
 */

trait StringValueTransformer[T]{
  def toValue(from: String): Option[T]
}