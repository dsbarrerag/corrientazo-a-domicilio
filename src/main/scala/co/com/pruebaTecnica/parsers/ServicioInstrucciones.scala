package co.com.pruebaTecnica.parsers

import java.io.{File, PrintWriter}

import co.com.pruebaTecnica.movimiento.{A, D, I, Movimiento}

import scala.io.Source
import scala.util.{Failure, Success, Try}

sealed trait ServicioInstruccionesAlgebra {

  def convertirAInstrucciones(archivo: String): Try[Seq[Seq[Movimiento]]]

  def escribirArchivo(archivo: String, texto: Seq[String])

  def leerArchivo(ruta: String): Try[List[String]]
}

sealed trait ServicioInstrucciones extends ServicioInstruccionesAlgebra {

  override def convertirAInstrucciones(archivo: String): Try[Seq[Seq[Movimiento]]] =
    leerArchivo(archivo).flatMap { lineas =>
      listTryTransofrmer(lineas.map(linea => parseLinea(linea)))
    }

  override def leerArchivo(ruta: String): Try[List[String]] =
    for {
      buffer <- Try(Source.fromFile(ruta))
      lines <- Try(buffer.getLines().toList)
    } yield lines

  private def parseLinea(linea: String): Try[Seq[Movimiento]] =
    listTryTransofrmer(linea.map(instruccion => parseLetra(instruccion)))

 private def parseLetra(instruccion: Char): Try[Movimiento] =
    Try(instruccion match {
      case 'A' => A()
      case 'I' => I()
      case 'D' => D()
      case _ => throw new IllegalArgumentException
    })

  def listTryTransofrmer[A](lista: Seq[Try[A]]): Try[Seq[A]] =
    lista.find(_.isFailure).fold[Try[Seq[A]]] {
      Success[Seq[A]](lista.filter(_.isSuccess).map(_.get))
    } { f => Failure[Seq[A]](new IllegalArgumentException) }

  override def escribirArchivo(archivo: String, texto: Seq[String]): Unit = {
    val writer = new PrintWriter(new File(archivo))
    for (linea <- texto) writer.write(s"$linea \n")
    writer.close()
  }
}

object ServicioInstrucciones extends ServicioInstrucciones
