package co.com.pruebaTecnica.dron

import co.com.pruebaTecnica.Posicion
import co.com.pruebaTecnica.errores.{NumeroAlmuerzosIncorrecto, ErrorDron}

// TODO id
case class Dron(posicion: Posicion, almuerzos: Int)

object Dron {

  def apply(): Dron = new Dron(Posicion(), Config.maxAlmuerzos)

  def apply(posicion: Posicion, almuerzos: Int): Either[ErrorDron, Dron] =
    if (almuerzos >= 0 && almuerzos <= Config.maxAlmuerzos)
      Right(new Dron(posicion, almuerzos))
    else
      Left(NumeroAlmuerzosIncorrecto())

  def apply(posicion: Either[ErrorDron, Posicion], almuerzos: Int): Either[ErrorDron, Dron] =
    posicion.map(p => new Dron(p, almuerzos))
}
