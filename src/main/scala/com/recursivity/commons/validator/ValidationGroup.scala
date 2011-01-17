package com.recursivity.commons.validator

import collection.mutable.MutableList


/**
 * Useful utility class for grouping Validators.
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
