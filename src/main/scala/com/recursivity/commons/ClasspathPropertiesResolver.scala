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

  def getProperties(context: Class[_], locale: List[String] = Nil): Properties = {
    var properties: Properties = null

    if (locale == Nil) {
      properties = loadProperties(context)
    } else {
      try {
        properties = loadProperties(context, Some(locale.head))
      } catch {
        case ex: NullPointerException => {
          val localeList = locale.drop(1)
          properties = getProperties(context, localeList)
        }
      }
    }

    return properties
  }

  private def loadProperties(context: Class[_], locale: Option[String] = None): Properties = {
    var properties = new Properties
    ClasspathResourceResolver.getAbsoluteResource(context, ".properties", locale) {
      is => properties.load(is)
    }

    return properties
  }

}
