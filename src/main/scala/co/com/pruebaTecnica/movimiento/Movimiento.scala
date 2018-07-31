package co.com.pruebaTecnica.movimiento


sealed trait Movimiento

case class A() extends Movimiento

sealed trait Giro extends Movimiento

case class I() extends Giro

case class D() extends Giro
