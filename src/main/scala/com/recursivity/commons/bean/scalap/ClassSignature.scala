package com.recursivity.commons.bean.scalap

/**
 * Created by IntelliJ IDEA.
 * User: wfaler
 * Date: 11/04/2011
 * Time: 23:11
 * To change this template use File | Settings | File Templates.
 */

object ClassSignature{
  def apply(cls: Class[_]): ClassSignature = {
    apply(cls.getName)
  }

  def apply(clsName: String): ClassSignature = {
    val signature = ScalaSigParser.process(clsName)
    null
  }
}

case class ClassSignature(clazz: String, members: Option[List[Definition]])

case class Definition(defType: DefType, returnType: String, name: String, parameters: Option[List[Parameter]])
case class Parameter(name: String, paramType: String)

sealed trait DefType
case object Func extends DefType
case object Val extends DefType
case object Var extends DefType

