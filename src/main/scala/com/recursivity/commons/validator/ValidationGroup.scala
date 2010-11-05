package com.recursivity.commons.validator

import collection.mutable.MutableList


/**
 * Created by IntelliJ IDEA.
 * User: wfaler
 * Date: Oct 30, 2010
 * Time: 5:57:51 PM
 * To change this template use File | Settings | File Templates.
 */

class ValidationGroup(messageResolver: MessageResolver = null){
  private val validators = new MutableList[Validator]
  
  def add(validator: Validator) = {
    validators += validator
  }
  
  def validateAndReturnFailures: List[Validator] ={
    val failed = new MutableList[Validator]
    validators.foreach(f => {
      if(!f.isValid)
        failed  += f
    })
    failed.toList
  }

  def validateAndReturnErrorMessages: List[Tuple2[String, String]] = {
    if(messageResolver == null)
      throw new IllegalStateException("No MessageResolver set for ValidationGroup")
    val failed = new MutableList[Tuple2[String, String]]
    validators.foreach(f => {
      if(!f.isValid){
        val tuple = (f.getKey, messageResolver.resolveMessage(f))
        failed += tuple
      }
    })
    failed.toList
  }
  
}