package com.recursivity.commons

import java.util.Properties
import collection.mutable.HashMap

/**
 * Created by IntelliJ IDEA.
 * User: wfaler
 * Date: Nov 4, 2010
 * Time: 5:21:44 PM
 * To change this template use File | Settings | File Templates.
 */

object ClasspathPropertiesResolver {
  val contexts = new HashMap[String, HashMap[String, Properties]]

  def getProperties(context: Class[_], locale: String): Properties = {
    var map: HashMap[String, Properties] = null
    try {
      map = contexts(context.getName)
    } catch {
      case e: NoSuchElementException => {
        map = new HashMap[String, Properties]
        contexts += context.getName -> map
        loadProperties(context, null)
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
          properties = loadProperties(context, locale)
        }
      }
    }
    return properties
  }

  private def loadProperties(context: Class[_], locale: String): Properties = {
    var map = contexts(context.getName)

    var properties = new Properties
    try {
      properties.load(ClasspathResourceResolver.getResource(context, ".properties", locale))
    } catch {
      case e: NullPointerException => {
        if (locale != null)
          properties = map("default")
        else
          throw new NullPointerException("No Validator message properties file found in classpath for context: " + context.getName + " _" + locale)
      }
    }
    if (locale == null) map += "default" -> properties
    else map += locale -> properties

    return properties
  }

}
