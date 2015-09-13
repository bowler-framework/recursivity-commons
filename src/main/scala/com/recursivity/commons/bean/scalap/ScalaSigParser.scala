package com.recursivity.commons.bean.scalap

import java.io.{StringWriter, PrintStream, OutputStreamWriter, ByteArrayOutputStream}
import scala.reflect.NameTransformer
import scala.tools.scalap._
import scalax.rules.scalasig._

/**
* This file is largely an extended retro-fit of scala.tools.scalap.Main from the scalap.jar of the Scala
* distribution, hence copyrights and kudos to all the people involved in that. I have merely made it slightly more
* usable within an application context, rather than as a command line tool, and added functions for parsing the output.
*
* Scala classfile decoder
* c) 2003-2010, LAMP/EPFL
* http://scala-lang.org/
*
* @author Matthias Zenger, Stephane Micheloud, Burak Emir, Ilya Sergey, Wille Faler
*/
object ScalaSigParser {

  val SCALA_SIG            = "ScalaSig"
  val SCALA_SIG_ANNOTATION = "Lscala/reflect/ScalaSignature;"
  val BYTES_VALUE          = "bytes"

  /* val versionMsg = "Scala classfile decoder " +
  Properties.versionString + " -- " +
    Properties.copyrightString + "\n"    */

  var printPrivates = false

  def isScalaFile(bytes: Array[Byte]): Boolean = {
    val byteCode  = ByteCode(bytes)
    val classFile = ClassFileParser.parse(byteCode)
    classFile.attribute("ScalaSig").isDefined
  }

  /**Processes the given Java class file.
    *
    * @param clazz the class file to be processed.
    */
  def processJavaClassFile(clazz: Classfile) = {
    val out = new StringWriter
    /// / construct a new output stream writer
    //val out = new OutputStreamWriter(Console.out)
    val writer = new JavaWriter(clazz, out)
    // print the class
    writer.printClass
    out.flush()
    writer.toString()
  }

  def isPackageObjectFile(s: String) = s != null && (s.endsWith(".package") || s == "package")

  def parseScalaSignature(scalaSig: ScalaSig, isPackageObject: Boolean) = {
    val baos   = new ByteArrayOutputStream
    val stream = new PrintStream(baos)
    val syms   = scalaSig.topLevelClasses ++ scalaSig.topLevelObjects

    syms.head.parent match {
      // Partial match
      case Some(p) if (p.name != "<empty>") => {
        val path = p.path
        if (!isPackageObject) {
          stream.print("package ");
          stream.print(path);
          stream.print("\n")
        } else {
          val i = path.lastIndexOf(".")
          if (i > 0) {
            stream.print("package ");
            stream.print(path.substring(0, i))
            stream.print("\n")
          }
        }
      }
      case _ =>
    }
    // Print classes
    val printer = new ScalaSigPrinter(stream, printPrivates)
    syms foreach (printer printSymbol _)
    baos.toString
  }

  def decompileScala(bytes: Array[Byte], isPackageObject: Boolean): String = {
    val byteCode = ByteCode(bytes)
    val classFile = ClassFileParser.parse(byteCode)

    scala.tools.scalap.scalax.rules.scalasig.ScalaSigParser.parse(classFile) match {
      case Some(scalaSig) => parseScalaSignature(scalaSig, isPackageObject)
      case None           => ""
    }
  }

  def readToBytes(stream: java.io.InputStream): Array[Byte] = {
    val len = stream.available
    val bytes = new Array[Byte](len)

    var cur = 0
    var finished = false
    while (!finished && cur != len) {
      val read = stream.read(bytes, cur, len - cur)
      finished = read == -1
      if (!finished)
        cur += read
    }
    assert(cur == len)

    bytes
  }

  /** Executes scalap with the given arguments and classpath for the
    *  class denoted by `classname`.
    */
  def process(classname: String) = {
    // find the classfile
    val encName = classname match {
      case "scala.AnyRef" => "java.lang.Object"
      case _ =>
        // we have to encode every fragment of a name separately, otherwise the NameTransformer
        // will encode using unicode escaping dot separators as well
        // we can afford allocations because this is not a performance critical code
        classname.split('.').map(NameTransformer.encode).mkString(".")
    }

    val name = "/" + encName.replace(".", "/") + ".class"
    val resource = this.getClass.getResourceAsStream(name)
    val bytes = readToBytes(resource)

    if (isScalaFile(bytes)) {
      decompileScala(bytes, isPackageObjectFile(encName))
    } else {
      // construct a reader for the classfile content
      val reader = new ByteArrayReader(bytes)
      // parse the classfile
      val clazz = new Classfile(reader)
      processJavaClassFile(clazz)
    }

  }

}


