package co.com.pruebaTecnica

import co.com.pruebaTecnica.errores.{DistanciaExcedida, ErrorDron}

sealed trait PuntoCardinal

case class N() extends PuntoCardinal {
  override def toString: String = "Norte"
}

case class S() extends PuntoCardinal {
  override def toString: String = "Sur"
}

case class E() extends PuntoCardinal {
  override def toString: String = "Oriente"
}

case class O() extends PuntoCardinal {
  override def toString: String = "Occidente"
}


case class Coordenada(x: Int, y: Int)

object Coordenada {

  def apply(): Coordenada = new Coordenada(0, 0)
}

case class Posicion(coordenada: Coordenada, orientacion: PuntoCardinal)

object Posicion {
  def apply(): Posicion = new Posicion(Coordenada(), N())
}

