package com.recursivity.commons.validator

import collection.mutable.MutableList


/**
 * Created by IntelliJ IDEA.
 * User: wfaler
 * Date: Oct 30, 2010
 * Time: 5:57:51 PM
 * To change this template use File | Settings | File Templates.
 */

case class ValidationGroup(messageResolver: MessageResolver = null){
  private val validators = new MutableList[Validator]
  
  def add(validator: Validator) = validators += validator

  def validateAndReturnFailures: List[Validator] = validators filter { !_.isValid } toList

  def validateAndReturnErrorMessages: List[Tuple2[String, String]] = {
    if (messageResolver == null) {
      throw new IllegalStateException("No MessageResolver set for ValidationGroup")
    }

    validators filter { !_.isValid } map { v => (v.getKey, messageResolver.resolveMessage(v)) } toList
  }
  
}
