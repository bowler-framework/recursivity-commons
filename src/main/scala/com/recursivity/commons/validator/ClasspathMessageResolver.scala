package com.recursivity.commons.validator

import java.util.Properties
import collection.mutable.HashMap
import com.recursivity.commons.ClasspathPropertiesResolver


/**
 * Created by IntelliJ IDEA.
 * User: wfaler
 * Date: Nov 4, 2010
 * Time: 3:27:43 PM
 * To change this template use File | Settings | File Templates.
 */

class ClasspathMessageResolver(context: Class[_], locale: String = null) extends MessageResolver {
  def resolveMessage(validator: Validator): String = {
    val properties = ClasspathPropertiesResolver.getProperties(context, locale)
    var message = properties.get(validator.getClass.getSimpleName).toString
    var key = validator.getKey
    try{
      key = properties.get(validator.getKey).toString
    }catch{
      case e: NullPointerException => {
        // do nothing, just use the plain key instead
      }
    }
    message = message.replace("{key}", key)
    validator.getReplaceModel.foreach(tuple => {
      message = message.replace("{" + tuple._1 + "}", "" + tuple._2)
    })

    return message
  }
}

