package com.recursivity.commons.bean

import collection.mutable.MutableList

/**
 * Created by IntelliJ IDEA.
 * User: wfaler
 * Date: Nov 29, 2010
 * Time: 11:48:44 PM
 * To change this template use File | Settings | File Templates.
 */

object BeanUtils{

  def instantiate(cls: Class[_]): Any ={
    val cons = cls.getConstructors.head
    val list = new MutableList[AnyRef]
      cons.getParameterTypes.foreach(cls =>{
        cls.getName match{
          case "long" => {
            val l : Long = 0
            list += l.asInstanceOf[AnyRef]
          }case "int" => {
            val l : Int = 0
            list += l.asInstanceOf[AnyRef]
          }case "float" => {
            val f = 0.0f
            list += f.asInstanceOf[AnyRef]
          }case "double" => {
            val f = 0.0d
            list += f.asInstanceOf[AnyRef]
          }case "boolean" => {
            val b = false
            list += b.asInstanceOf[AnyRef]
          }case "short" => {
            val l : Short = 0
            list += l.asInstanceOf[AnyRef]
          }case "byte" => {
            val b = new java.lang.Byte("0")
            list += b.asInstanceOf[AnyRef]
          }case "char" => {
            val c = new java.lang.Character('c')
            list += c
          }case "scala.Option" => list += None
          case _ => list += null

        }
    })
    println(list.size)
    return cons.newInstance(list.toArray:_*) 
  }
}