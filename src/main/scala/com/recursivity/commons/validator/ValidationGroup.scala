package com.recursivity.commons.validator

import collection.mutable.MutableList

/**
 * Created by IntelliJ IDEA.
 * User: wfaler
 * Date: Oct 30, 2010
 * Time: 5:57:51 PM
 * To change this template use File | Settings | File Templates.
 */

class ValidationGroup{
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
}