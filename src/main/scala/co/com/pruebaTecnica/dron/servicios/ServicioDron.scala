package co.com.pruebaTecnica.dron.servicios

import co.com.pruebaTecnica._
import co.com.pruebaTecnica.config.Config
import co.com.pruebaTecnica.dron.Dron
import co.com.pruebaTecnica.dron.errores.{DistanciaExcedida, ErrorDron}
import co.com.pruebaTecnica.ruta._

sealed trait ServicioDronAlgebra {

  type Entrega = Seq[Movimiento]

  def entregarRutaAlmuerzos(dron: Dron, entrega: Seq[Seq[Movimiento]]): Seq[Either[ErrorDron, Dron]]

}

sealed trait ServicioDron extends ServicioDronAlgebra {

  override def entregarRutaAlmuerzos(dron: Dron, ruta: Seq[Seq[Movimiento]]): Seq[Either[ErrorDron, Dron]] =
    ruta
      .scanLeft[Either[ErrorDron, Dron], Seq[Either[ErrorDron, Dron]]](Right(dron))((eDron, mov) =>
      eDron.flatMap(d => hacerRuta(d, mov)))
      .tail

  private def hacerRuta(dron: Dron, instrucciones: Seq[Movimiento]): Either[ErrorDron, Dron] =
    instrucciones
      .foldLeft[Either[ErrorDron, Dron]](Right(dron))((Edron, movimiento) =>
      Edron.flatMap(d => moverDron(d, movimiento)))
      .flatMap(entregarAlmuerzo)

  private def entregarAlmuerzo(dron: Dron): Either[ErrorDron, Dron] =
    Dron(dron.id, dron.posicion, dron.almuerzos - 1)

  private def moverDron(dron: Dron, movimiento: Movimiento): Either[ErrorDron, Dron] =
    movimiento match {
      case A() => moverAlfrente(dron)
      case giro: Giro => girarDron(dron, giro)
    }

  private def girarDron(dron: Dron, giro: Giro): Either[ErrorDron, Dron] =
    giro match {
      case D() => girarDerecha(dron)
      case I() => girarIzquierda(dron)
    }

  def girarIzquierda(dron: Dron): Either[ErrorDron, Dron] = {
    val posicionNueva = dron.posicion.orientacion match {
      case N() => O()
      case S() => E()
      case E() => N()
      case O() => S()
    }
    Right(dron.copy(posicion = Posicion(dron.posicion.coordenada, posicionNueva)))
  }

  def girarDerecha(dron: Dron): Either[ErrorDron, Dron] = {
    val posicionNueva = dron.posicion.orientacion match {
      case N() => E()
      case S() => O()
      case E() => S()
      case O() => N()
    }
    Right(dron.copy(posicion = Posicion(dron.posicion.coordenada, posicionNueva)))
  }

  private def moverAlfrente(dron: Dron): Either[ErrorDron, Dron] = {
    val c: Coordenada = dron.posicion.coordenada
    val nuevaCoordenada = dron.posicion.orientacion match {
      case N() => Coordenada(c.x, c.y + 1)
      case S() => Coordenada(c.x, c.y - 1)
      case E() => Coordenada(c.x + 1, c.y)
      case O() => Coordenada(c.x - 1, c.y)
    }
    if (validarCoordenada(nuevaCoordenada))
      Right(dron.copy(posicion = dron.posicion.copy(coordenada = nuevaCoordenada)))
    else
      Left(DistanciaExcedida())
  }

  private def validarCoordenada(coordenada: Coordenada): Boolean = {
    coordenada.x >= Config.gridxMin && coordenada.x <= Config.gridXMax &&
      coordenada.y >= Config.gridYMin && coordenada.y <= Config.gridYMax
  }

}

object ServicioDron extends ServicioDron

