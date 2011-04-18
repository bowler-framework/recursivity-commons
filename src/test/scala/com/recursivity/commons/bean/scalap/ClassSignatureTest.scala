package com.recursivity.commons.bean.scalap

import org.scalatest.FunSuite
import com.recursivity.commons.bean.{NestedGenerics, IntPrimitiveBean}

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

    assert(sig.members.size == 1)
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

    assert(sig.members.size == 1)
    assert(sig.members.head.name == "myVar")
    assert(sig.members.head.returnType.clazz == "scala.Predef.String")
    assert(sig.members.head.returnType.genericTypes == None)
    assert(sig.members.head.defType == Var)

  }

}

class Hello
case class CaseClass(hello: String)

case class CaseWithVar(var myVar: String)
