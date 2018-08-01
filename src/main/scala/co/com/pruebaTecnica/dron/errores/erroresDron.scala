package co.com.pruebaTecnica.dron.errores

sealed trait ErrorDron {
  val error: String
}

case class DistanciaExcedida() extends ErrorDron {
  override val error: String = "Las coordenadads del dron se salen del rango aceptado"
}

case class NumeroAlmuerzosIncorrecto() extends ErrorDron {
  override val error: String = "Numero de almuerzos incorrecto"
}
