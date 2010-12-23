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

object GenericsParser extends RegexParsers {
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

  def parseDefinition(cls: ParameterizedType): GenericTypeDefinition = parseDefinition(cls.toString)

  def parseDefinition(cls: String): GenericTypeDefinition = {
    parse(clazz, cls) match {
      case Success(definition, _) => return definition
      case Failure(msg, _) => throw new IllegalArgumentException("Failure: " + msg)
      case Error(msg, _) => throw new IllegalArgumentException("Error: " + msg)
    }
  }

}

case class GenericTypeDefinition(clazz: String, genericTypes: Option[List[GenericTypeDefinition]])