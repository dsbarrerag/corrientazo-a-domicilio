package co.com.pruebaTecnica

import co.com.pruebaTecnica.dron.Dron
import co.com.pruebaTecnica.errores.ErrorDron
import co.com.pruebaTecnica.movimiento.Movimiento

sealed trait ServicioDronAlgebra {

  def entregarRutaAlmuerzos(dron: Dron, entrega: Seq[Seq[Movimiento]]): Seq[String]

}

sealed trait ServicioDron extends ServicioDronAlgebra {

  override def entregarRutaAlmuerzos(dron: Dron, instrucciones: Seq[Seq[Movimiento]]): Seq[String] = {
    instrucciones.map { linea =>
      moverRuta(dron, linea)
        .fold(err => err.error, d => traerCoordenadas(d))
    }
  }

  protected def moverRuta(dron: Dron, instrucciones: Seq[Movimiento]): Either[ErrorDron, Dron] =
    instrucciones
      .foldLeft[Either[ErrorDron, Dron]](Right(dron))((Edron, movimiento) =>
      Edron.flatMap(d => moverDron(d)(movimiento)))
      .flatMap(entregarAlmuerzo)

  protected def moverDron(dron: Dron)(movimiento: Movimiento): Either[ErrorDron, Dron] =
    movimiento.mover(dron.posicion).map(p => dron.copy(posicion = p))

  protected def entregarAlmuerzo(dron: Dron): Either[ErrorDron, Dron] =
    Dron(dron.posicion, dron.almuerzos - 1)

  protected def traerCoordenadas(dron: Dron): String =
    s"(${dron.posicion.coordenada.x}, ${dron.posicion.coordenada.y}) direccion ${dron.posicion.orientacion}"
}

object ServicioDron extends ServicioDron

