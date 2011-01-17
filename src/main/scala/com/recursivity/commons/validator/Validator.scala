package com.recursivity.commons.validator

/**
 * validates a value
 */
trait Validator {

  /**
   * checks validity
   */
  def isValid: Boolean

  /**
   * returns a list of replace-string values to be replaced in any message, such as "min", "max" for length related validators
   */
  def getReplaceModel: List[Tuple2[String, Any]]

  /**
   * Returns the key, such as a property name or property-name key that this validator is bound to
   */
  def getKey: String
}
