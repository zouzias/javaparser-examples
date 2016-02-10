package org.zouzias.javaparsers.examples

import japa.parser.ast.`type`.Type
import japa.parser.ast.body._
import scala.collection.JavaConverters._
import scala.collection.mutable.ArrayBuffer

case class Variable(name: String, tp: Type){
  override def toString(): String = {
    s"${name}: ${tp}"
  }
}

case class Method(name: String, args: Array[Variable], retType: Type) {

  override def toString(): String = {
    val buf = new StringBuffer()
    buf.append(s"Method: ${this.name}(")
    this.args.foreach { arg =>
      buf.append(s"${arg.name}: ${arg.tp}")
    }
    buf.append(")\n")
    buf.toString()
  }
}

object Method extends Serializable {

  def apply(method: MethodDeclaration): Method = {
    val params = Option(method.getParameters)

    val args: Array[Variable] = params match {
      case Some(x) => x.asScala.map { case param: Parameter =>
        Variable(param.getId.getName, param.getType)
      }.toArray[Variable]
      case None => Array.empty[Variable]
    }

    Method(method.getName, args, method.getType)
  }
}

case class Clazz(name: String, variables: Array[Variable], methods: Array[Method]){

  override def toString(): String = {
    val buf = new StringBuffer()

    buf.append(s"Class: ${this.name}\n")
    buf.append("Variables: ")
    buf.append(variables.map(_.toString()).mkString(" , ")).append("\n")
    methods.foreach(m => buf.append(m.toString()))
    buf.toString
  }

}

object Clazz extends Serializable {

  private def parseVariables(fd: FieldDeclaration): List[Variable] = {
    val vars = Option(fd.getVariables)

    vars match {
      case Some(vs) => vs.asScala.map(v => Variable(v.getId.getName, fd.getType)).toList
      case None => List.empty[Variable]
    }
  }

  def apply(td: TypeDeclaration): Clazz = {
    val className = td.getName
    val members = Option(td.getMembers)

    val classMethods = new ArrayBuffer[Method]()
    val classVariables = new ArrayBuffer[Variable]()
    members.foreach{ mmbrs =>
      mmbrs.asScala.foreach{
        case m: MethodDeclaration => classMethods.append(Method(m))
        case fd: FieldDeclaration => classVariables.appendAll(parseVariables(fd))
        case c: ClassOrInterfaceDeclaration => println(s"Ignoring inner class ${c.getName}")
        case _ => println("Not none")
      }
    }

    Clazz(className, classVariables.toArray, classMethods.toArray)
  }
}

