package com.recursivity.commons.bean

/**
 * Created by IntelliJ IDEA.
 * User: wfaler
 * Date: 26/02/2011
 * Time: 00:01
 * To change this template use File | Settings | File Templates.
 */

object BeanCopyer{

  def copy[T <: AnyRef](source: T): T = {
    val cls = source.getClass
    val copy = BeanUtils.instantiate[T](cls)
    setValues(cls, source, copy)
    return copy
  }

  private def setValues(cls: Class[_], source: AnyRef, copy: AnyRef): Unit = {
    cls.getDeclaredFields.foreach(field =>{
      field.setAccessible(true)
      field.set(copy, field.get(source))
    })
    if(cls.getSuperclass != null)
      setValues(cls.getSuperclass, source, copy)
  }

}