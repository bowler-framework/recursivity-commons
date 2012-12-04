package com.recursivity.commons.bean

import util.parsing.combinator.RegexParsers
import scala.util.parsing.combinator.syntactical._
import collection.mutable.MutableList
import java.lang.reflect.ParameterizedType

/**
 * Created by IntelliJ IDEA.
 * User: wfaler
 * Date: Dec 4, 2010
 * Time: 12:13:50 AM
 * To change this template use File | Settings | File Templates.
 */

object GenericTypeDefinition extends RegexParsers {
  val ID = """[a-zA-Z]([a-zA-Z0-9]|_[a-zA-Z0-9])*""" r

  def clazz: Parser[GenericTypeDefinition] = className ~ opt(genericType) ^^ {case s ~ n => GenericTypeDefinition(s, n)}

  def genericType  = "<" ~ (paramList | clazz) ^^ {
    case s ~ (p: Product) => {
      def collect(list: MutableList[GenericTypeDefinition], p: Product): MutableList[GenericTypeDefinition] =
        (list /: p.productIterator) {
          case (l, el: GenericTypeDefinition) => l += el
          case (l, p: Product)                => collect(l, p)
          case (l,_)                          => l
        }
      collect(new MutableList[GenericTypeDefinition], p).toList
    }
  }

  def paramList: Parser[List[GenericTypeDefinition]] = repsep(clazz, ",") ^^ {case (ts: List[GenericTypeDefinition]) => ts}


  def className: Parser[String] = repsep(ID, ".") ^^ { case (ts: List[String]) => ts mkString "." }

  def apply(cls: ParameterizedType): GenericTypeDefinition = apply(cls.toString)


  def apply(cls: String): GenericTypeDefinition = {
    var parseThis = cls.replace("[", "<")
    parseThis = parseThis.replace("]", ">")
    parse(clazz, parseThis) match {
      case Success(definition, _) => return definition
      case Failure(msg, _) => throw new IllegalArgumentException("Failure: " + msg)
      case Error(msg, _) => throw new IllegalArgumentException("Error: " + msg)
    }
  }

}

case class GenericTypeDefinition(clazz: String, genericTypes: Option[List[GenericTypeDefinition]]){
  def toSimpleString(lowerCaseInitial: Boolean = false): String = {
    var simpleName: String = clazz
    if(clazz.contains(".")){
      simpleName = clazz.substring(clazz.lastIndexOf(".") + 1)
    }
    if(lowerCaseInitial)
      simpleName = simpleName.substring(0,1).toLowerCase + simpleName.substring(1)
    if(genericTypes != None){
      simpleName = simpleName + "["
      var first = true
      genericTypes.get.foreach(b => {
        if(!first)
          simpleName = simpleName + ","
        simpleName = simpleName + b.toSimpleString(lowerCaseInitial)
        first = false
      })
      simpleName = simpleName + "]"
    }
    return simpleName
  }

  def definedClass = getClassForType(clazz)

  private def getClassForType(cls: String): Class[_] = {
    var fieldCls: Class[_] = null
    cls match {
      case "long" => fieldCls = classOf[Long]
      case "int" => fieldCls = classOf[java.lang.Integer]
      case "float" => fieldCls = classOf[java.lang.Float]
      case "double" => fieldCls = classOf[java.lang.Double]
      case "boolean" => fieldCls = classOf[Boolean]
      case "short" => fieldCls = classOf[java.lang.Short]
	  case "byte" => fieldCls = classOf[java.lang.Byte]
      case "scala.Long" => fieldCls = classOf[Long]
      case "scala.Int" => fieldCls = classOf[java.lang.Integer]
      case "scala.Float" => fieldCls = classOf[java.lang.Float]
      case "scala.Double" => fieldCls = classOf[java.lang.Double]
      case "scala.Boolean" => fieldCls = classOf[java.lang.Boolean]
      case "scala.Short" => fieldCls = classOf[java.lang.Short]
      case "scala.Byte" => fieldCls = classOf[java.lang.Byte]
      case "scala.List" => fieldCls = classOf[List[_]]
      case "scala.Option" => fieldCls = classOf[Option[_]]
      case "scala.Seq" => fieldCls = classOf[Seq[_]]
      case "scala.Set" => fieldCls = classOf[Set[_]]
      case "scala.Predef.String" => fieldCls = classOf[java.lang.String]
      case _ => fieldCls = Class.forName(cls)
    }
    fieldCls
  }
}