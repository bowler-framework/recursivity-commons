package com.recursivity.commons.bean

import org.scalatest.FunSuite

/**
 * Created by IntelliJ IDEA.
 * User: wfaler
 * Date: Dec 4, 2010
 * Time: 12:12:55 AM
 * To change this template use File | Settings | File Templates.
 */

class GenericsParserTest extends FunSuite{


  test("parse  nested generics"){
    //val generics = "scala.collection.immutable.List<scala.collection.immutable.Map<java.lang.String>>"
    val generics = "scala.collection.immutable.List<scala.collection.immutable.Map<java.lang.String, java.lang.String>>"
    val typeDef = GenericsParser.parseDefinition(generics)
    assert(typeDef != null)

    assert(typeDef.clazz.equals("scala.collection.immutable.List"))
    assert(typeDef.genericTypes.get.size == 1)
    assert(typeDef.genericTypes.get(0).clazz.equals("scala.collection.immutable.Map"))

    val tp = typeDef.genericTypes.get(0)
    assert(tp.genericTypes.get.size == 2)
    tp.genericTypes.get.foreach(str =>{
      assert(str.clazz.equals("java.lang.String"))
      assert(str.genericTypes.equals(None))
    })
  }

  test("one level nested generics"){
    val generics = "scala.collection.immutable.List<scala.collection.immutable.Map<java.lang.String>>"
    val typeDef = GenericsParser.parseDefinition(generics)
    assert(typeDef != null)

    assert(typeDef.clazz.equals("scala.collection.immutable.List"))
    assert(typeDef.genericTypes.get.size == 1)
    assert(typeDef.genericTypes.get(0).clazz.equals("scala.collection.immutable.Map"))

    val tp = typeDef.genericTypes.get(0)
    assert(tp.genericTypes.get.size == 1)
    tp.genericTypes.get.foreach(str =>{
      assert(str.clazz.equals("java.lang.String"))
      assert(str.genericTypes.equals(None))
    })
  }

  test("simple generics"){
    val generics = "scala.collection.immutable.List<java.lang.String>"
    val typeDef = GenericsParser.parseDefinition(generics)
    assert(typeDef != null)

    assert(typeDef.clazz.equals("scala.collection.immutable.List"))

    assert(typeDef.genericTypes.get.size == 1)
    assert(typeDef.genericTypes.get(0).clazz.equals("java.lang.String"))
    assert(typeDef.genericTypes.get(0).genericTypes.equals(None))
  }


  test("GenericTypeDef.toSimpleString"){
    val typeDef = GenericsParser.parseDefinition("scala.collection.immutable.List<scala.collection.immutable.Map<java.lang.String, java.lang.String>>")
    assert(typeDef.toSimpleString.equals("List[Map[String,String]]"))
  }


}


