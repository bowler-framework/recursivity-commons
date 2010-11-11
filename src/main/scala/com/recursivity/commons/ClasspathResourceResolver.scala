package com.recursivity.commons

import java.io.InputStream

/**
 * Created by IntelliJ IDEA.
 * User: wfaler
 * Date: Nov 5, 2010
 * Time: 6:51:43 PM
 * To change this template use File | Settings | File Templates.
 */

object ClasspathResourceResolver {

  /**
   * Gets a resource with no fallback to default if localised file does not exist
   */
  def getAbsoluteResource(context: Class[_], fileType: String, locale: String = null)(op: InputStream => Any) = {
    var is: InputStream = null
    try {
      val path = getUri(context, fileType, locale)
      val obj = new ClasspathResourceResolver
      is = obj.getClass.getResourceAsStream(path)
      op(is)
    } finally {
      is.close
    }
  }

  def getUri(context: Class[_], fileType: String, locale: String = null): String = {
    var path: String = null
    var fileEnding: String = null
    if (fileType.startsWith("."))
      fileEnding = fileType
    else
      fileEnding = "." + fileType

    if (locale == null) {
      path = "/" + context.getName.replace(".", "/") + fileEnding
    } else {
      path = "/" + context.getName.replace(".", "/") + "_" + locale + fileEnding
    }
    return path
  }

  /**
   * get potentially localised file, fallback to default if not present
   */
  def resolveResource(context: Class[_], fileType: String, locale: String = null)(op: InputStream => Any) = {
    if (locale == null)
      getAbsoluteResource(context, fileType, locale) {is => op(is)}
    else {
      try {
        getAbsoluteResource(context, fileType, locale) {is => op(is)}
      } catch {
        case e: NullPointerException => {
          getAbsoluteResource(context, fileType, null) {is => op(is)}
        }
      }
    }
  }

}

class ClasspathResourceResolver