package co.com.pruebaTecnica.dao

import java.io.{File, PrintWriter}

import scala.io.Source
import scala.util.Try

sealed trait RepositorioArchivosAlgebra {
  def leerArchivo(ruta: String, nombre: String): Try[Seq[String]]

  def escribirArchivo(ruta: String, nombre: String, contenido: Seq[String]): Try[String]
}

sealed trait RepositorioArchivos extends RepositorioArchivosAlgebra {
  override def leerArchivo(ruta: String, nombre: String): Try[Seq[String]] =
    for {
      buffer <- Try(Source.fromFile(ruta + nombre))
      lines <- Try(buffer.getLines().toList)
    } yield lines

  override def escribirArchivo(ruta: String, nombre: String, contenido: Seq[String]): Try[String] = {
    val filename = s"out${nombre}.txt"
    for {
      writer <- Try(new PrintWriter(new File(ruta + filename)))
      _ = escribirLinea(contenido, writer)
      _ = writer.close()
    } yield filename
  }

  private def escribirLinea(contenido: Seq[String], printWriter: PrintWriter) = {
    Try {
      for (linea <- contenido) printWriter.write(s"$linea\n")
    }
  }
}

object RepositorioArchivos extends RepositorioArchivos