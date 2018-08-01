package co.com.pruebaTecnica.dron

import co.com.pruebaTecnica.Posicion
import co.com.pruebaTecnica.ruta.Movimiento

case class Entrega(dron: Dron, ruta: Seq[Seq[Movimiento]])

case class ResultadoEntrega(dron: Dron, finales: Seq[Posicion])