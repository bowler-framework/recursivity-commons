package com.recursivity.commons.validator

import com.recursivity.commons.ClasspathPropertiesResolver


/**
 * Created by IntelliJ IDEA.
 * User: wfaler
 * Date: Nov 4, 2010
 * Time: 3:27:43 PM
 * To change this template use File | Settings | File Templates.
 */

class ClasspathMessageResolver(context: Class[_], locale: List[String] = List()) extends MessageResolver {
  def resolveMessage(validator: Validator): String = {
    val properties = ClasspathPropertiesResolver.getProperties(context, locale)
    var message = properties.get(validator.getClass.getSimpleName).toString
    val key = properties.getProperty(validator.getKey, validator.getKey)

    message = message.replace("{key}", key)
    validator.getReplaceModel.foreach(tuple => {
      message = message.replace("{" + tuple._1 + "}", "" + tuple._2)
    })

    return message
  }
}

