package com.recursivity.commons.validator

/**
 * Created by IntelliJ IDEA.
 * User: wfaler
 * Date: Oct 30, 2010
 * Time: 5:16:34 PM
 * To change this template use File | Settings | File Templates.
 */

trait Validator{
  def isValid: Boolean

  /*
    * Identifying key for this validator, such as a key to an error message in a properties file.
   */
  def getKey: String
}