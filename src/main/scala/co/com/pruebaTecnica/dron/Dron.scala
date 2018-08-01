package co.com.pruebaTecnica.dron

import co.com.pruebaTecnica.Posicion
import co.com.pruebaTecnica.config.Config
import co.com.pruebaTecnica.dron.errores.{ErrorDron, NumeroAlmuerzosIncorrecto}

case class Dron(id: Int, posicion: Posicion, almuerzos: Int){
  override def toString: String =
    s"(${this.posicion.coordenada.x}, ${this.posicion.coordenada.y}) direccion ${this.posicion.orientacion}"
}

object Dron {

  def apply(id: Int): Dron = new Dron(id: Int, Posicion(), Config.maxAlmuerzos)

  def apply(id: Int, posicion: Posicion, almuerzos: Int): Either[ErrorDron, Dron] =
    if (almuerzos >= 0 && almuerzos <= Config.maxAlmuerzos)
      Right(new Dron(id: Int, posicion, almuerzos))
    else
      Left(NumeroAlmuerzosIncorrecto())

}
