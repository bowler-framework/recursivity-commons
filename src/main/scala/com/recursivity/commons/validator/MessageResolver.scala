package com.recursivity.commons.validator

/**
 * Created by IntelliJ IDEA.
 * User: wfaler
 * Date: Nov 4, 2010
 * Time: 3:26:55 PM
 * To change this template use File | Settings | File Templates.
 */

trait MessageResolver{
  def resolveMessage(validator: Validator): String
}