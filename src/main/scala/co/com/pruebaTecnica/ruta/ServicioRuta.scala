package co.com.pruebaTecnica.ruta

import co.com.pruebaTecnica.dao.RepositorioArchivos

import scala.util.{Failure, Success, Try}

sealed trait ServicioRutaAlgebra {

  def traerInstrucciones(ruta: String, archivo: String): Try[Seq[Seq[Movimiento]]]
}

sealed trait ServicioRuta extends ServicioRutaAlgebra {

  override def traerInstrucciones(ruta: String, archivo: String): Try[Seq[Seq[Movimiento]]] =
    RepositorioArchivos.leerArchivo(ruta, archivo).flatMap { lineas =>
      listTryTransofrmer(lineas.map(linea => parseLinea(linea)))
    }

  private def parseLinea(linea: String): Try[Seq[Movimiento]] =
    listTryTransofrmer(linea.map(instruccion => parseLetra(instruccion)))

  private def parseLetra(instruccion: Char): Try[Movimiento] =
    instruccion match {
      case 'A' => Try(A())
      case 'I' => Try(I())
      case 'D' => Try(D())
      case _ => Try(throw new IllegalArgumentException)
    }

  private def listTryTransofrmer[A](lista: Seq[Try[A]]): Try[Seq[A]] =
    lista.find(_.isFailure).fold[Try[Seq[A]]] {
      Success[Seq[A]](lista.filter(_.isSuccess).map(_.get))
    } { f => Failure[Seq[A]](new IllegalArgumentException) }

}

object ServicioRuta extends ServicioRuta
