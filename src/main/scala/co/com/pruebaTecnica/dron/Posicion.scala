package co.com.pruebaTecnica

import co.com.pruebaTecnica.errores.{DistanciaExcedida, ErrorDron}

sealed trait PuntoCardinal

case class Norte() extends PuntoCardinal

case class Sur() extends PuntoCardinal

case class Oriente() extends PuntoCardinal

case class Occidente() extends PuntoCardinal

case class Coordenada(x: Int = 0, y: Int = 0)

object Coordenada {

  def apply(): Coordenada = new Coordenada(0, 0)

  def apply(x: Int, y: Int): Either[ErrorDron, Coordenada] =
    if ((-10 until 10 contains x) && (-10 until 10 contains y))
      Right(new Coordenada(x, y))
    else
      Left(DistanciaExcedida())
}

case class Posicion(coordenada: Coordenada, orientacion: PuntoCardinal)

object Posicion {

  def apply(): Posicion = new Posicion(Coordenada(), Norte())

  def apply(coordenada: Either[ErrorDron, Coordenada], orientacion: PuntoCardinal): Either[ErrorDron, Posicion] =
    coordenada.map(c => new Posicion(c, orientacion))

}

