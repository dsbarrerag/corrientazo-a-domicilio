package co.com.pruebaTecnica.dron

import co.com.pruebaTecnica._
import co.com.pruebaTecnica.config.Config
import co.com.pruebaTecnica.dron.servicios.ServicioDron
import co.com.pruebaTecnica.ruta.{A, D, I}
import org.scalatest.FunSpec

class DronSpecs extends FunSpec {
  describe("Un Dron") {
    it("Deberia poder ser creado con un id") {
      val d1 = Dron(1)
      val d2 = Dron(1, Posicion(Coordenada(0, 0), N()), Config.maxAlmuerzos)
      assert(d2 === Right(d1))
    }
  }

  describe("El servicio de Drones") {
    describe("Dado un dron") {
      val dron = Dron(1)

      describe("Dada una ruta de entregas") {
        val ruta = Seq(
          Seq(A(), A(), A(), A(), I(), A(), A(), D()),
          Seq(D(), D(), A(), I(), A(), D()),
          Seq(A(), A(), I(), A(), D(), A(), D())
        )
        it("Deberia realizar las entregas y retornar un arreglo con las coordenadas finales") {
          val d2 = ServicioDron.entregarRutaAlmuerzos(dron, ruta)
          val res = List(
            "(-2, 4) direccion Norte",
            "(-1, 3) direccion Sur",
            "(0, 0) direccion Occidente")
          assert(d2 === res)
        }
      }
    }
  }
}