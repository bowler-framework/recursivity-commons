package com.recursivity.commons.bean

import org.scalatest.FunSuite

/**
 * Created by IntelliJ IDEA.
 * User: wfaler
 * Date: 25/02/2011
 * Time: 23:56
 * To change this template use File | Settings | File Templates.
 */

class BeanCopyerTest extends FunSuite{

  test("constructor choice"){
    val copy = BeanCopyer.copy[Author](Author(3,"Wille", "Faler", None))
    assert(copy.id == 3)
    assert(copy.firstName == "Wille")
    assert(copy.lastName == "Faler")

    val cp = BeanCopyer.copy[Author](new Author)
    assert(cp.id == 0)
    assert(cp.firstName == "John")
    assert(cp.lastName == "Doe")

  }
}

case class Author(val id: Long, firstName: String, lastName: String, email: Option[String]){
  def this() = this(0,"John","Doe",Some("johndoe@gmail.com"))
  def this(id: Long) =  this(id,"John","Doe",Some("johndoe@gmail.com"))
}