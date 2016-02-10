package org.zouzias.javaparsers.examples

import java.io.{FileInputStream, File}

import japa.parser.JavaParser
import japa.parser.ast.CompilationUnit
import japa.parser.ast.body._
import org.apache.commons.io.FileUtils
import scala.collection.JavaConverters._
import scala.collection.mutable.ArrayBuffer

/**
 *
 */
object MethodApp {

  // scalastyle:off println
  def main(args: Array[String]): Unit = {

    if (args.length < 1) {
      println("Please provide a directory as input")
      return
    }

    println("Input root dir is " + args(0))

    val inputFolder: String = args(0)
    val rootFolder: File = new File(inputFolder)

    val javaFiles = FileUtils.listFiles(rootFolder, Array[String]("java"), true).asScala

    println("Number of java files are : " + javaFiles.size)

    for (javaFile <- javaFiles) {
      val in = new FileInputStream(javaFile)
      val cu = getCompilationUnit(in)

      cu.foreach { c =>
        val classes = c.getTypes.asScala.toArray

        for (clazz <- classes) {
          println(Clazz(clazz))
        }
      }
    }
  }
  // scalastyle:on println


  private def getCompilationUnit(in: FileInputStream): Option[CompilationUnit] = {
    try {
      Some(JavaParser.parse(in))
    }
    catch {
      case e: Exception => None
    } finally {
      in.close
    }
  }
}

