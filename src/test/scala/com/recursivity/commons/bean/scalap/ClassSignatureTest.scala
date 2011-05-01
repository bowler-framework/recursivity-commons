package com.recursivity.commons.bean.scalap

import org.scalatest.FunSuite
import com.recursivity.commons.bean.{GenericTypeDefinition, NestedGenerics, IntPrimitiveBean}

/**
 * Created by IntelliJ IDEA.
 * User: wfaler
 * Date: 15/04/2011
 * Time: 19:32
 * To change this template use File | Settings | File Templates.
 */

class ClassSignatureTest extends FunSuite{

  test("simple class"){
    val sig = ClassSignature(classOf[Hello])
    assert(sig != null)
    assert(sig.clazz == "com.recursivity.commons.bean.scalap.Hello")
  }

  test("case class"){
    val sig = ClassSignature(classOf[CaseClass])
    assert(sig.clazz == "com.recursivity.commons.bean.scalap.CaseClass")
    assert(sig.constructor.size == 1)
    assert(sig.constructor.head.name == "hello")
    assert(sig.constructor.head.paramType.clazz == "scala.Predef.String")
    assert(sig.constructor.head.paramType.genericTypes == None)

    assert(sig.members.size == 2)
    assert(sig.members.head.name == "hello")
    assert(sig.members.head.returnType.clazz == "scala.Predef.String")
    assert(sig.members.head.returnType.genericTypes == None)
    assert(sig.members.head.defType == Val)
  }

  test("case class with var constructor"){
    val sig = ClassSignature(classOf[CaseWithVar])
    assert(sig.clazz == "com.recursivity.commons.bean.scalap.CaseWithVar")
    assert(sig.constructor.size == 1)
    assert(sig.constructor.head.name == "myVar")
    assert(sig.constructor.head.paramType.clazz == "scala.Predef.String")
    assert(sig.constructor.head.paramType.genericTypes == None)

    assert(sig.members.size == 2)
    assert(sig.members.head.name == "myVar")
    assert(sig.members.head.returnType.clazz == "scala.Predef.String")
    assert(sig.members.head.returnType.genericTypes == None)
    assert(sig.members.head.defType == Var)

  }

  test("case class with generified list"){
    val sig = ClassSignature(classOf[GenerifiedClass])
    assert("scala.List" == sig.members.head.returnType.clazz)
    assert("scala.Predef.String" == sig.members.head.returnType.genericTypes.get.head.clazz)
  }

  test("function parsing"){
    val sig = ClassSignature(classOf[CaseWithFunctions])
    assert(sig.members.size == 9)
    val predefString = GenericTypeDefinition("scala.Predef.String", None)
    val javaString = GenericTypeDefinition("java.lang.String", None)
    val scalaInt =  GenericTypeDefinition("scala.Int", None)
    val unit = GenericTypeDefinition("scala.Unit", None)
    val intParam = Parameter("int", scalaInt)
    val intList = GenericTypeDefinition("scala.List", Some(List(GenericTypeDefinition("scala.Int", None))))
    val intParamList = List(intParam)
    val intAndListParams = List(intParam, intList)

    sig.members.foreach(member => {
      member match{
        case Member(Val, "int",_,_) => assert(member.returnType.clazz == "scala.Int")
        case Member(Def, "copy", _, _) => {}// do nothing
        case Member(Def, "funcWithoutParams", javaString, List()) => {}// do nothing
        case Member(Def, "funcWithoutParamsWithReturnType", predefString, List()) => {}// do nothing
        case Member(Def, "funcWithoutReturn", GenericTypeDefinition("scala.Unit", None), List()) => {}// do nothing
        case Member(Def, "funcParams", javaString, intParamList) => {}// do nothing
        case Member(Def, "funcParamsUnit", unit, intParamList) => {}// do nothing
        case Member(Def, "funcParamsUnit2", unit, intParamList) => {}// do nothing
        case Member(Def, "funcParamsExplicitReturn", predefString, intAndListParams) => {}// do nothing
        case _ => fail
      }
      member.returnType

    })
  }

  test("funky function signatures"){
    val sig = ClassSignature(classOf[FunkyFunctionSignatures])

    val getFunc = sig.members.find(p => p.name.startsWith("GET"))
    getFunc match{
      case Some(member) => {
        assert(member.name == "GET /hello/:id/* /_=-><+")
        assert(member.returnType.definedClass == classOf[java.lang.String])
        assert(member.parameters.size == 1)
        assert(member.parameters(0).name == "int")
        assert(member.parameters(0).paramType.definedClass == classOf[java.lang.Integer])

      }
      case None => fail
    }

    val putFunc = sig.members.find(p => p.name.startsWith("PUT"))
    putFunc match{
      case Some(member) => {
        assert(member.name == "PUT /hello/myworld")
        assert(member.returnType.definedClass == classOf[java.lang.String])
        assert(member.parameters.size == 0)
      }
      case None => fail
    }
  }

  test("Member.reflectedName for strange signatures"){
    val sig = ClassSignature(classOf[FunkyFunctionSignatures])

    val get = "GET$u0020$divhello$div$colonid$div$times$u0020$div_$eq$minus$greater$less$plus"
    val put = "PUT$u0020$divhello$divmyworld"

    val getFunc = sig.members.find(p => p.name.startsWith("GET"))
    val putFunc = sig.members.find(p => p.name.startsWith("PUT"))
    println(getFunc.get.reflectedName)
    assert(get == getFunc.get.reflectedName)
    assert(put == putFunc.get.reflectedName)
  }

}

class FunkyFunctionSignatures{
  def `GET /hello/:id/* /_=-><+`(int: Int) = "hello"

  def `PUT /hello/myworld` = "world"
}

class Hello
case class CaseClass(hello: String)

case class CaseWithVar(var myVar: String)

case class GenerifiedClass(hello: List[String])

case class CaseWithFunctions(int: Int){
  def funcWithoutParams = "hello"
  def funcWithoutParamsWithReturnType: String = "hello2"
  def funcWithoutReturn {}
  def funcParams(int: Int, list: List[String]) = "hello"
  def funcParamsUnit(int: Int): Unit = {

  }

  def funcParamsUnit2(int: Int) = {

  }

  def funcParamsExplicitReturn(int: Int, list: List[Int]): String = "hello3"
}
