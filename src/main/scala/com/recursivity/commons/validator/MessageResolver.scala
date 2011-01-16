package com.recursivity.commons.validator

/**
 * resolves an error message given a validator
 */

trait MessageResolver{
  def resolveMessage(validator: Validator): String
}