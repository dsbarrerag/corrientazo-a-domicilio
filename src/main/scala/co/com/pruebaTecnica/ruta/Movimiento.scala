package co.com.pruebaTecnica.ruta

sealed trait Movimiento

case class A() extends Movimiento

sealed trait Giro extends Movimiento

case class I() extends Giro

case class D() extends Giro

