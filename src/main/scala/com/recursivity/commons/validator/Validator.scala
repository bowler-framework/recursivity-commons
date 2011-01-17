package com.recursivity.commons.validator

/**
 * Created by IntelliJ IDEA.
 * User: wfaler
 * Date: Oct 30, 2010
 * Time: 5:16:34 PM
 * To change this template use File | Settings | File Templates.
 */

trait Validator {
  def isValid: Boolean

  /**
   * returns a list of replace-string values to be replaced in any message, such as "min", "max" for length related validators
   */
  def getReplaceModel: List[Tuple2[String, Any]]

  /**
   * Returns the key, such as a property name that this validator is bound to
   */
  def getKey: String
}
