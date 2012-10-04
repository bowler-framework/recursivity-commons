package com.recursivity.commons.bean

import collection.immutable._
import collection.{TraversableLike}
import collection.mutable.{DoubleLinkedList, LinkedList, Builder, MutableList}
import java.lang.reflect.{Field, Constructor, ParameterizedType}

/**
 * Utility class that is able to create new instances of arbitrary objects and fill them with values/vars based on
 * a map of values.
 */

object BeanUtils {
  def instantiate[T](cls: Class[_]): T = {
    var cons: Constructor[_] = null
    cls.getConstructors.foreach(c =>{
      if (cons == null) cons = c
      if(c.getParameterTypes.size > cons.getParameterTypes.size) cons = c
    })
    val list = new MutableList[AnyRef]
    cons.getParameterTypes.foreach(cls => {
      cls.getName match {
        case "long" => {
          val l: Long = 0
          list += l.asInstanceOf[AnyRef]
        }
        case "int" => {
          val l: Int = 0
          list += l.asInstanceOf[AnyRef]
        }
        case "float" => {
          val f = 0.0f
          list += f.asInstanceOf[AnyRef]
        }
        case "double" => {
          val f = 0.0d
          list += f.asInstanceOf[AnyRef]
        }
        case "boolean" => {
          val b = false
          list += b.asInstanceOf[AnyRef]
        }
        case "short" => {
          val l: Short = 0
          list += l.asInstanceOf[AnyRef]
        }
        case "byte" => {
          val b = new java.lang.Byte("0")
          list += b.asInstanceOf[AnyRef]
        }
        case "char" => {
          val c = new java.lang.Character('c')
          list += c
        }
        case "scala.Option" => list += None
        case _ => list += null

      }
    })
    return cons.newInstance(list.toArray: _*).asInstanceOf[T]
  }

  def setProperty(cls: Class[_], bean: Any, key: String, value: Any) {
    try {
      val field = cls.getDeclaredField(key)
      val fieldCls = getClassForJavaPrimitive(field.getType)
      field.setAccessible(true)
      if (classOf[ParameterizedType].isAssignableFrom(field.getGenericType.getClass) || fieldCls.equals(classOf[Array[_]])) {
        val parameterized = field.getGenericType.asInstanceOf[ParameterizedType]
        setGenerified(field, bean, fieldCls, GenericTypeDefinition(parameterized), value)

      } else {
        val transformer = TransformerRegistry(fieldCls)
        field.set(bean, transformer.getOrElse(throw new BeanTransformationException(fieldCls)).toValue(value.toString).getOrElse(null))
      }
    } catch {
      case e: NoSuchFieldException => {
        if (cls.getSuperclass != null)
          setProperty(cls.getSuperclass, bean, key, value)
      }case ie: IllegalArgumentException => {}// do nothing, do not set value with illegal argument
    }
  }


  private def setGenerified(field: Field, bean: Any, fieldCls: Class[_], typeDef: GenericTypeDefinition, value: Any) {
    if(!typeDefHasObject(typeDef))
      field.set(bean, resolveGenerifiedValue(fieldCls, typeDef, value))
    else{
	throw new Exception("Busted with scala 2.9.2")
	/*
      val signature = ClassSignature(bean.asInstanceOf[AnyRef].getClass)
      var member: Option[Member] = None
      signature.members.foreach(f => {
        if(f.name == field.getName)
          member = Some(f)
      })
      field.set(bean, resolveGenerifiedValue(fieldCls, member.getOrElse(throw new
          IllegalArgumentException("Could not resolve generic type for: " + member)).returnType, value))
	*/
    }
  }

  private def typeDefHasObject(typeDef: GenericTypeDefinition): Boolean = {
    var result = false
    if(typeDef.clazz == "java.lang.Object")
      return true
    typeDef.genericTypes.getOrElse(return false).foreach(f => {
      if(!result)
        result = typeDefHasObject(f)
    })
    result
  }

  def resolveGenerifiedValue(cls: Class[_], genericType: GenericTypeDefinition, input: Any): Any = {
    if (classOf[TraversableLike[_ <: Any, _ <: Any]].isAssignableFrom(cls)) {
      val list = valueList(genericType, input)
      return resolveTraversableOrArray(cls, list)
    } else if (classOf[java.util.Collection[_ <: Any]].isAssignableFrom(cls)) {
      val list = valueList(genericType, input)
      return resolveJavaCollectionType(cls, list)
    } else if (classOf[Option[_ <: Any]].isAssignableFrom(cls)) {
      val c = genericType.genericTypes.get.head.definedClass
      if (genericType.genericTypes.get.head.genericTypes.equals(None)) {
        val transformer = TransformerRegistry(c)
        return transformer.getOrElse(throw new BeanTransformationException(c)).toValue(input.toString)
      } else {
        val t = genericType.genericTypes.get.head
        val targetCls = t.definedClass
        return Some(resolveGenerifiedValue(targetCls, t, input))
      }
    } else {
      return null
    }
  }

  def resolveJavaCollectionType(cls: Class[_], list: scala.collection.Seq[_]): Any = {
    if(classOf[java.util.Set[_]].isAssignableFrom(cls)){
      var set: java.util.Set[Any] = null
      try{
        set = cls.newInstance.asInstanceOf[java.util.Set[Any]]
      }catch{
        case e: InstantiationException => set = new java.util.HashSet[Any]
      }
      list.foreach(b => set.add(b))
      return set
    }else{
      var l: java.util.List[Any] = null
      try{
        l = cls.newInstance.asInstanceOf[java.util.List[Any]]
      }catch{
        case e: InstantiationException => l = new java.util.ArrayList[Any]
      }
      list.foreach(b => l.add(b))
      return l
    }
  }

  def valueList(genericType: GenericTypeDefinition, input: Any): MutableList[Any] = {
    val c = genericType.genericTypes.get.head.definedClass
    val transformer = TransformerRegistry(c)
    val list = new MutableList[Any]
    if (input.isInstanceOf[List[_]]) {
      val l = input.asInstanceOf[List[_]]
      l.foreach(f => list += transformer.getOrElse(throw new BeanTransformationException(c)).toValue(f.toString).getOrElse(null))
    } else if (input.isInstanceOf[Array[_]]) {
      val array = input.asInstanceOf[Array[_]]
      array.foreach(f => list += transformer.getOrElse(throw new BeanTransformationException(c)).toValue(f.toString).getOrElse(null))
    }
    return list
  }

  // due to the trickiness of supporting immutable sets/lists, types are hard coded here with no support for extension
  // of immutable Scala Sets/Lists, TreeSet is not supported
  //
  def resolveTraversableOrArray(cls: Class[_], list: scala.collection.Seq[_]): Any = {
    if (cls.equals(classOf[List[_]])) {
      return list.toList
    } else if (cls.equals(classOf[Set[_]]))
      return list.toSet
    else if (cls.equals(classOf[Array[_]]))
      return list.toArray
    else if (cls.equals(classOf[ListSet[_]]))
      return new ListSet ++ list.toList
    else if (cls.equals(classOf[HashSet[_]]))
      return new HashSet ++ list.toList
    else if (cls.equals(classOf[scala.collection.mutable.HashSet[_]]))
      return new scala.collection.mutable.HashSet ++ list.toList
    else if (cls.equals(classOf[scala.collection.Seq[_]]) || cls.equals(classOf[Seq[_]])) {
      return list.toList
    }else {
      val listOrSet = cls.newInstance
      if (classOf[Builder[Any, Any]].isAssignableFrom(cls)) {
        val builder = listOrSet.asInstanceOf[Builder[Any, Any]]
        list.foreach(b => builder += b)
        return builder
      } else if (classOf[LinkedList[_]].isAssignableFrom(cls)) {
        var seq = listOrSet.asInstanceOf[LinkedList[_]]
        list.foreach(elem => {
          seq = seq :+ elem
        })
        return seq
      } else if (classOf[DoubleLinkedList[_]].isAssignableFrom(cls)) {
        var seq = listOrSet.asInstanceOf[DoubleLinkedList[_]]
        list.foreach(elem => {
          seq = seq :+ elem
        })
        return seq
      }
    }
  }

  def instantiate[T](cls: Class[_], properties: Map[String, Any]): T = {
    val bean = instantiate[T](cls)

    return setProperties[T](bean, properties)
  }

  def setProperties[T](bean: T, properties: Map[String, Any]): T = {
    properties.keys.foreach(key => {
      setProperty(bean.asInstanceOf[AnyRef].getClass, bean, key, properties(key))
    })

    return bean
  }




  private def getClassForJavaPrimitive(cls: Class[_]): Class[_] = {

    var fieldCls: Class[_] = null
    cls.getName match {
      case "long" => fieldCls = classOf[Long]
      case "int" => fieldCls = classOf[java.lang.Integer]
      case "float" => fieldCls = classOf[java.lang.Float]
      case "double" => fieldCls = classOf[java.lang.Double]
      case "boolean" => fieldCls = classOf[Boolean]
      case "short" => fieldCls = classOf[java.lang.Short]
      case _ => fieldCls = cls
    }
    return fieldCls
  }
}
