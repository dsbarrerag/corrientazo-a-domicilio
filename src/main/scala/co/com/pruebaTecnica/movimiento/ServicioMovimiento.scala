package co.com.pruebaTecnica.movimiento

import co.com.pruebaTecnica.Posicion

trait ServicioMovimientoAlgebra {
  def mover(posicion: Posicion, movimiento: Posicion => Posicion)
}

