package co.com.pruebaTecnica.parsers

import co.com.pruebaTecnica.movimiento.{A, D, I, Movimiento}

sealed trait ServicioInstruccionesAlgebra {

  def parseLetra(instruccion: Char): Movimiento

  def parseLinea(linea: String): Seq[Movimiento]

}

sealed trait ServicioInstrucciones extends ServicioInstruccionesAlgebra {

  override def parseLetra(instruccion: Char): Movimiento =
    instruccion match {
      case 'A' => A
      case 'I' => I
      case 'D' => D
    }

  override def parseLinea(linea: String): Seq[Movimiento] =
    linea.map(instruccion => parseLetra(instruccion))
}

object ServicioInstrucciones extends ServicioInstrucciones
