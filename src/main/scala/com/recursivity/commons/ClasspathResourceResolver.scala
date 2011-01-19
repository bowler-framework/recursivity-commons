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
  def getAbsoluteResource(context: Class[_], fileType: String, locale: Option[String] = None)(op: InputStream => Any) = {
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

  def getUri(context: Class[_], fileType: String, locale: Option[String] = None): String = {
    val fileEnding = if (fileType.startsWith(".")) fileType else "." + fileType
    val loc = locale match {
      case None => ""
      case Some(x) => "_" + x
    }

    val path = "/" + context.getName.replace(".", "/") + loc + fileEnding
    return path
  }

  /**
   * get potentially localised file, fallback to default if not present
   */
  def resolveResource(context: Class[_], fileType: String, locale: List[String] = List())(op: InputStream => Any): Any = {
    if (locale == Nil)
      getAbsoluteResource(context, fileType, None) {is => op(is)}
    else {
      try {
        getAbsoluteResource(context, fileType, Some(locale.head)) {is => op(is)}
      } catch {
        case e: NullPointerException => {
          val localeList = locale.drop(1)
          resolveResource(context, fileType, localeList) {is => op(is)}
        }
      }
    }
  }


}

class ClasspathResourceResolver
