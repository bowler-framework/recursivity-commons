package com.recursivity.commons.validator


import com.recursivity.commons.ClasspathPropertiesResolver


/**
 * Resolves error messages for a validator from the classpath, given a classpath context and optional list of locale preferences.
 * localisation enabled.
 */

class ClasspathMessageResolver(context: Class[_], locale: List[String] = List()) extends MessageResolver {
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

