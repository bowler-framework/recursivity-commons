package com.recursivity.commons.validator

import java.util.Properties
import collection.mutable.HashMap

/**
 * Created by IntelliJ IDEA.
 * User: wfaler
 * Date: Nov 4, 2010
 * Time: 3:27:43 PM
 * To change this template use File | Settings | File Templates.
 */

class ClasspathMessageResolver(context: Class[_], locale: String = null) extends MessageResolver {
  def resolveMessage(validator: Validator): String = {
    val properties = ClasspathMessageResolver.getProperties(context, validator, locale)
    var message = properties.get(validator.getClass.getSimpleName).toString
    val key = properties.get(validator.getKey).toString
    message = message.replace("{key}", key)
    validator.getReplaceModel.foreach(tuple => {
      message = message.replace("{" + tuple._1 + "}", "" + tuple._2)
    })

    return message
  }
}

object ClasspathMessageResolver {
  val contexts = new HashMap[String, HashMap[String, Properties]]

  def getProperties(context: Class[_], validator: Validator, locale: String): Properties = {
    var map: HashMap[String, Properties] = null
    try {
      map = contexts(context.getName)
    } catch {
      case e: NoSuchElementException => {
        map = new HashMap[String, Properties]
        contexts += context.getName -> map
        loadProperties(context, validator, null)
      }
    }
    var properties: Properties = null
    if (locale == null) {
      properties = map("default")
    }else{
      try {
        properties = map(locale)
      } catch {
        case e: NoSuchElementException => {
          properties = loadProperties(context, validator, locale)
        }
      }
    }
    return properties
  }

  private def loadProperties(context: Class[_], validator: Validator, locale: String): Properties = {
    var path: String = null
    var map = contexts(context.getName)
    if (locale == null){
      path = "/" + context.getName.replace(".", "/") + ".properties"
    }else{
      path = "/" + context.getName.replace(".", "/") + "_" + locale + ".properties"
    }
    println(path + " locale: " + locale)

    var properties = new Properties
    try {
      properties.load(validator.getClass.getResourceAsStream(path))
    } catch {
      case e: NullPointerException => {
        println(path + " " + e)
        if (locale != null)
          properties = map("default")
        else
          throw new NullPointerException("No Validator message properties file found in classpath: " + path)
      }
    }
    if (locale == null) map += "default" -> properties
    else map += locale -> properties

    return properties
  }

}