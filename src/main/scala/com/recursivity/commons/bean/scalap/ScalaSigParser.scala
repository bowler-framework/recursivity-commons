package com.recursivity.commons.bean.scalap

import scala.tools.scalap.scalax.rules.scalasig._


import tools.scalap._
import scalax.rules.scalasig.ClassFileParser.{ConstValueIndex, Annotation}
import reflect.generic.ByteCodecs
import java.io.{StringWriter, ByteArrayOutputStream, PrintStream}

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

  val SCALA_SIG = "ScalaSig"
  val SCALA_SIG_ANNOTATION = "Lscala/reflect/ScalaSignature;"
  val BYTES_VALUE = "bytes"

 /* val versionMsg = "Scala classfile decoder " +
    Properties.versionString + " -- " +
    Properties.copyrightString + "\n"    */

  var printPrivates = false


  def processJavaClassFile(clazz: Classfile): String = {
    val out = new StringWriter
    //val out = new OutputStreamWriter(Console.out)
    val writer = new JavaWriter(clazz, out)
    // print the class
    writer.printClass
    out.flush()
    return writer.toString
  }

  def process(classname: String): String = {
    val encName = Names.encode(
      if (classname == "scala.AnyRef") "java.lang.Object"
      else classname)
    val name = "/" + encName.replace(".", "/") + ".class"
    val resource = this.getClass.getResourceAsStream(name)

    val bytes = readToBytes(resource)

    if (isScalaFile(bytes)) {
      return decompileScala(bytes, isPackageObjectFile(classname))
    } else {
      // construct a reader for the classfile content
      val reader = new ByteArrayReader(bytes) //cfile.toByteArray)
      // parse the classfile
      val clazz = new Classfile(reader)
      return processJavaClassFile(clazz)
    }
  }

  def readToBytes(stream: java.io.InputStream): Array[Byte] = {
    val len = stream.available
    val bytes = new Array[Byte](len)

    var cur = 0
    while (cur != -1)
      cur += stream.read(bytes, cur, len - cur)

    bytes
  }

  def isPackageObjectFile(s: String) = s != null && (s.endsWith(".package") || s == "package")


  def decompileScala(bytes: Array[Byte], isPackageObject: Boolean): String = {
    val byteCode = ByteCode(bytes)
    val classFile = ClassFileParser.parse(byteCode)
    classFile.attribute(SCALA_SIG).map(_.byteCode).map(ScalaSigAttributeParsers.parse) match {
      // No entries in ScalaSig attribute implies that the signature is stored in the annotation
      case Some(ScalaSig(_, _, entries)) if entries.length == 0 => unpickleFromAnnotation(classFile, isPackageObject)
      case Some(scalaSig) => parseScalaSignature(scalaSig, isPackageObject)
      case None => ""
    }
  }

  def isScalaFile(bytes: Array[Byte]): Boolean = {
    val byteCode = ByteCode(bytes)
    val classFile = ClassFileParser.parse(byteCode)
    classFile.attribute("ScalaSig").isDefined
  }

  def unpickleFromAnnotation(classFile: ClassFile, isPackageObject: Boolean): String = {
    import classFile._
    classFile.annotation(SCALA_SIG_ANNOTATION) match {
      case None => ""
      case Some(Annotation(_, elements)) =>
        val bytesElem = elements.find(elem => constant(elem.elementNameIndex) == BYTES_VALUE).get
        val bytes = ((bytesElem.elementValue match {
          case ConstValueIndex(index) => constantWrapped(index)
        })
          .asInstanceOf[StringBytesPair].bytes)
        val length = ByteCodecs.decode(bytes)
        val scalaSig = ScalaSigAttributeParsers.parse(ByteCode(bytes.take(length)))
        parseScalaSignature(scalaSig, isPackageObject)
    }
  }

  def parseScalaSignature(scalaSig: ScalaSig, isPackageObject: Boolean) = {
    val baos = new ByteArrayOutputStream
    val stream = new PrintStream(baos)
    val syms = scalaSig.topLevelClasses ::: scalaSig.topLevelObjects
    syms.head.parent match {
      //Partial match
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
    for (c <- syms) {
      printer.printSymbol(c)
    }
    baos.toString
  }

}