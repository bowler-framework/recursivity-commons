package com.recursivity.commons.bean.scalap

import java.util.StringTokenizer
import collection.mutable.MutableList
import com.recursivity.commons.bean.GenericTypeDefinition

import util.parsing.combinator.RegexParsers
import scala.util.parsing.combinator.syntactical._


/**
 * Created by IntelliJ IDEA.
 * User: wfaler
 * Date: 11/04/2011val ID = """[a-zA-Z]([a-zA-Z0-9]|_[a-zA-Z0-9])*"""r
 * Time: 23:11
 * To change this template use File | Settings | File Templates.
 */

object ClassSignature extends RegexParsers {
  val lineBreak = System.getProperty("line.separator")
  val ID = """[a-zA-Z\[\]]([a-zA-Z0-9\[\]]|_[a-zA-Z0-9\[\]])*"""r

  def parameter: Parser[Parameter] = (opt("val" | "var") ~ ID ~ ":" ~ className) ^^ {case (x ~ s ~  f ~ n) => {Parameter(s, GenericTypeDefinition(n))}}

  def parameters: Parser[List[Parameter]] = ("(" ~> repsep(parameter, ",") <~")") ^^ {case s => s}

  def className: Parser[String] = repsep(ID, ".") ^^ { case (ts: List[String]) => ts mkString "." }

  def member: Parser[Member] = (valParse | varParse) ^^ {case s => s}

  def valParse: Parser[Member] = ("val" ~> ID ~ ":" ~ className) ^^ {case (s ~ t ~ u ) => {Member(Val, s, GenericTypeDefinition(u), List[Parameter]())}}

  def varParse: Parser[Member] = ("var" ~> ID ~ ":" ~ className) ^^ {case (s ~ t ~ u ) => {Member(Var, s, GenericTypeDefinition(u), List[Parameter]())}}

  def apply(cls: Class[_]): ClassSignature = {
    apply(cls.getName)
  }

  def apply(clsName: String): ClassSignature = {
    val signature = ScalaSigParser.process(clsName)
    val tokenizer = new StringTokenizer(signature, lineBreak)
    val members = new MutableList[Member]
    var constructor = List[Parameter]()
    while(tokenizer.hasMoreTokens){
      val token = tokenizer.nextToken.trim
      token match{
        case PrimaryConstructor(s) => constructor = s
        case MemberDefinition(definition) => members += definition
        case _ => {}// ignore
      }
    }
    return ClassSignature(clsName, constructor.toList, members.toList)
  }

  def parseParameters(params: String): List[Parameter] = {
    parse(parameters, params) match{
      case Success(definition, _) => return definition
      case Failure(msg, _) => throw new IllegalArgumentException("Failure: " + msg)
      case Error(msg, _) => throw new IllegalArgumentException("Error: " + msg)
    }
  }

  def parseMembers(memberString: String): Member = {
    parse(member, memberString) match{
      case Success(definition, _) => return definition
      case Failure(msg, _) => throw new IllegalArgumentException("Failure: " + msg)
      case Error(msg, _) => throw new IllegalArgumentException("Error: " + msg)
    }
  }


}

case class ClassSignature(clazz: String, constructor: List[Parameter], members: List[Member])

case class Member(defType: DefType, name: String, returnType: GenericTypeDefinition, parameters: List[Parameter])
case class Parameter(name: String, paramType: GenericTypeDefinition)

sealed trait DefType
case object Def extends DefType
case object Val extends DefType
case object Var extends DefType


object MemberDefinition{
  def unapply(input: String): Option[Member] = {
    if(input.startsWith("val") || input.startsWith("var"))
      return Some(ClassSignature.parseMembers(input))
    return None
  }
}


object PrimaryConstructor{
  def unapply(pkg: String): Option[List[Parameter]] = {
    if(pkg.startsWith("case class")){
      return parseClass(pkg, 11)
    }else if(pkg.startsWith("class")){
      return parseClass(pkg, 6)
    }else if(pkg.startsWith("trait")){
      return parseClass(pkg, 6)
    }else if(pkg.startsWith("object")){
      return parseClass(pkg, 7)
    }
    return None
  }

  private def parseClass(clazz: String, offset: Int): Option[List[Parameter]] = {
    if(clazz.indexOf("(") > 0){
      return Some(ClassSignature.parseParameters(clazz.substring(clazz.indexOf("("))))
    }else{
      return Some(List[Parameter]())
    }
  }
}

