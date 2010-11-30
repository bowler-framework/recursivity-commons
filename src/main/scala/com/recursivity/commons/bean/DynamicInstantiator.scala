package com.recursivity.commons.bean

import collection.mutable.MutableList

/**
 * Created by IntelliJ IDEA.
 * User: wfaler
 * Date: Nov 29, 2010
 * Time: 11:48:44 PM
 * To change this template use File | Settings | File Templates.
 */

object DynamicInstantiator{
  def instantiate(cls: Class[_]): Any ={
    val cons = cls.getConstructors.head
    val list = new MutableList[AnyRef]
    cons.getParameterTypes.foreach(cls =>{
      if(cls.getName.equals("long")){
        val l : Long = 0
        list += l.asInstanceOf[AnyRef]
      }else if(cls.getName.equals("int")){
        val l : Int = 0
        list += l.asInstanceOf[AnyRef]        
      }else if(cls.getName.equals("float")){
        val f = 0.0f
        list += f.asInstanceOf[AnyRef]
      }else if(cls.getName.equals("double")){
        val f = 0.0d
        list += f.asInstanceOf[AnyRef]
      }else if(cls.getName.equals("boolean")){
        val b = false
        list += b.asInstanceOf[AnyRef]
      }else if(cls.getName.equals("short")){
        val l : Short = 0
        list += l.asInstanceOf[AnyRef]
      }else if(cls.getName.equals("byte")){
        val b = new java.lang.Byte("0")
        list += b.asInstanceOf[AnyRef]
      }else if(cls.getName.equals("char")){
        val c = new java.lang.Character('c')
        list += c
      }else if(cls.getName.equals("scala.Option")){
        list += None
      }else list += null
    })

    return cons.newInstance(list.toArray:_*)    
  }
}