package co.com.pruebaTecnica.movimiento

import co.com.pruebaTecnica._
import co.com.pruebaTecnica.errores.ErrorDron


sealed trait Movimiento {
  def mover(posicion: Posicion): Either[ErrorDron, Posicion]
}

trait A extends Movimiento {
  override def mover(posicion: Posicion): Either[ErrorDron, Posicion] = {
    val c: Coordenada = posicion.coordenada
    posicion.orientacion match {
      case _: Norte => Posicion(Coordenada(c.x, c.y + 1), posicion.orientacion)
      case _: Sur => Posicion(Coordenada(c.x, c.y - 1), posicion.orientacion)
      case _: Oriente => Posicion(Coordenada(c.x + 1, c.y), posicion.orientacion)
      case _: Occidente => Posicion(Coordenada(c.x - 1, c.y), posicion.orientacion)
    }
  }
}

object A extends A

trait I extends Movimiento {
  override def mover(posicion: Posicion): Either[ErrorDron, Posicion] =
    posicion.orientacion match {
      case _: Norte => Right(posicion.copy(orientacion = Occidente()))
      case _: Sur => Right(posicion.copy(orientacion = Oriente()))
      case _: Oriente => Right(posicion.copy(orientacion = Norte()))
      case _: Occidente => Right(posicion.copy(orientacion = Sur()))
    }
}

object I extends I

trait D extends Movimiento {
  override def mover(posicion: Posicion): Either[ErrorDron, Posicion] =
    posicion.orientacion match {
    case _: Norte => Right(posicion.copy(orientacion = Oriente()))
    case _: Sur => Right(posicion.copy(orientacion = Occidente()))
    case _: Oriente => Right(posicion.copy(orientacion = Sur()))
    case _: Occidente => Right(posicion.copy(orientacion = Norte()))
  }
}

object D extends D