package com.recursivity.commons

/**
 * Created by IntelliJ IDEA.
 * User: wfaler
 * Date: Nov 5, 2010
 * Time: 11:58:16 PM
 * To change this template use File | Settings | File Templates.
 */

class ClasspathTextResource(context: Class[_], fileType: String, locale: String = null) extends TextResource with StringInputStreamReader{
  def load = ClasspathResourceResolver.resolveResource(context, fileType, locale){ is => this.load(is)}.asInstanceOf[String]
}