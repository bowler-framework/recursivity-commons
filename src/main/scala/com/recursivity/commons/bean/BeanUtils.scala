package com.recursivity.commons.bean

import java.lang.reflect.{ParameterizedType}
import collection.immutable._
import collection.{TraversableLike}
import collection.mutable.{DoubleLinkedList, LinkedList, Builder, MutableList}

/**
 * Created by IntelliJ IDEA.
 * User: wfaler
 * Date: Nov 29, 2010
 * Time: 11:48:44 PM
 * To change this template use File | Settings | File Templates.
 */

object BeanUtils {
  def instantiate[T](cls: Class[_]): T = {
    val cons = cls.getConstructors.head
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

  private def setProperty(cls: Class[_], bean: Any, key: String, value: Any) {
    try {
      val field = cls.getDeclaredField(key)
      val fieldCls = getClassForJavaPrimitive(field.getType)
      field.setAccessible(true)
      if (classOf[ParameterizedType].isAssignableFrom(field.getGenericType.getClass) || fieldCls.equals(classOf[Array[_]])) {
        val parameterized = field.getGenericType.asInstanceOf[ParameterizedType]
        field.set(bean, resolveGenerifiedValue(fieldCls, GenericsParser.parseDefinition(parameterized), value))
      } else {
        val transformer = TransformerRegistry.resolveTransformer(fieldCls)
        field.set(bean, transformer.getOrElse(throw new BeanTransformationException(fieldCls)).toValue(value.toString))
      }
    } catch {
      case e: NoSuchFieldException => {
        if (cls.getSuperclass != null)
          setProperty(cls.getSuperclass, bean, key, value)
      }
    }
  }

  private def resolveGenerifiedValue(cls: Class[_], genericType: GenericTypeDefinition, input: Any): Any = {
    if (classOf[TraversableLike[_ <: Any, _ <: Any]].isAssignableFrom(cls)) {
      val list = valueList(genericType, input)
      return resolveTraversableOrArray(cls, list)

    } else if (classOf[java.util.Collection[_ <: Any]].isAssignableFrom(cls)) {
      val list = valueList(genericType, input)
      return resolveJavaCollectionType(cls, list)
    } else if (classOf[Option[_ <: Any]].isAssignableFrom(cls)) {
      val c = Class.forName(genericType.genericTypes.get.head.clazz)
      if (genericType.genericTypes.get.head.genericTypes.equals(None)) {
        val transformer = TransformerRegistry.resolveTransformer(c)
        return Some(transformer.getOrElse(throw new BeanTransformationException(c)).toValue(input.toString))
      } else {
        val t = genericType.genericTypes.get.head
        val targetCls = Class.forName(t.clazz)
        return Some(resolveGenerifiedValue(targetCls, t, input))
      }
    } else {
      return null
    }
  }

  def resolveJavaCollectionType(cls: Class[_], list: MutableList[_]): Any = {
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
    val c = Class.forName(genericType.genericTypes.get.head.clazz)
    val transformer = TransformerRegistry.resolveTransformer(c)
    val list = new MutableList[Any]
    if (input.isInstanceOf[List[_]]) {
      val l = input.asInstanceOf[List[_]]
      l.foreach(f => list += transformer.getOrElse(throw new BeanTransformationException(c)).toValue(f.toString))
    } else if (input.isInstanceOf[Array[_]]) {
      val array = input.asInstanceOf[Array[_]]
      array.foreach(f => list += transformer.getOrElse(throw new BeanTransformationException(c)).toValue(f.toString))
    }
    return list
  }

  // due to the trickiness of supporting immutable sets/lists, types are hard coded here with no support for extension
  // of immutable Scala Sets/Lists, TreeSet is not supported
  //
  def resolveTraversableOrArray(cls: Class[_], list: MutableList[_]): Any = {
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
    else if(classOf[Seq[_]].isAssignableFrom(cls))
      return list.toList
    else {
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