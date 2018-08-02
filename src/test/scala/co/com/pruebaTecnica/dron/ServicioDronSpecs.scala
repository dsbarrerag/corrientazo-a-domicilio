package co.com.pruebaTecnica.dron

import co.com.pruebaTecnica._
import co.com.pruebaTecnica.config.Config
import co.com.pruebaTecnica.dron.errores.{DistanciaExcedida, NumeroAlmuerzosIncorrecto}
import co.com.pruebaTecnica.dron.servicios.ServicioDron
import co.com.pruebaTecnica.ruta.{A, D, I}
import org.scalatest.FunSpec

class ServicioDronSpecs extends FunSpec {
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
          val res = Seq(
            Dron(1, Posicion(Coordenada(-2, 4), N()), Config.maxAlmuerzos),
            Dron(1, Posicion(Coordenada(-1, 3), S()), Config.maxAlmuerzos),
            Dron(1, Posicion(Coordenada(0, 0), O()), Config.maxAlmuerzos)
          )
          assert(d2.toString === res.toString)
        }
      }

      it("Deberia poder moverse y avanzar hacia la derecha") {
        val ruta = Seq(
          Seq(A()),
          Seq(D(), A()),
          Seq(D(), A()),
          Seq(D(), A())
        )
        val d1 = ServicioDron.entregarRutaAlmuerzos(dron, ruta)
        val res = List(
          Dron(1, Posicion(Coordenada(0, 1), N()), Config.maxAlmuerzos),
          Dron(1, Posicion(Coordenada(1, 1), E()), Config.maxAlmuerzos),
          Dron(1, Posicion(Coordenada(1, 0), S()), Config.maxAlmuerzos),
          Dron(1, Posicion(Coordenada(0, 0), O()), Config.maxAlmuerzos)
        )
        assert(d1.toString === res.toString)
      }

      it("Deberia poder moverse y avanzar hacia la izquierda") {
        val ruta = Seq(
          Seq(A()),
          Seq(I(), A()),
          Seq(I(), A()),
          Seq(I(), A())
        )
        val d1 = ServicioDron.entregarRutaAlmuerzos(dron, ruta)
        val res = List(
          Dron(1, Posicion(Coordenada(0, 1), N()), Config.maxAlmuerzos),
          Dron(1, Posicion(Coordenada(-1, 1), O()), Config.maxAlmuerzos),
          Dron(1, Posicion(Coordenada(-1, 0), S()), Config.maxAlmuerzos),
          Dron(1, Posicion(Coordenada(0, 0), E()), Config.maxAlmuerzos)
        )
        assert(d1.toString === res.toString)
      }

      it("Deberia devolver un left cuando se pida entregar mas lejos del limite"){
        val ruta = Seq(
          Seq(A(), A(), A(), A(), A(), A(), A(), A(), A(), A(), A())
        )
        val d1 = ServicioDron.entregarRutaAlmuerzos(dron, ruta)
        assert(d1.last === Left(DistanciaExcedida()))
      }

      it("Deberia devolver un left cuando se pida entregar mas del maximo de envios"){
        val ruta = Seq(
          Seq(A()),
          Seq(A()),
          Seq(A()),
          Seq(I()),
          Seq(A()),
          Seq(A()),
          Seq(A()),
          Seq(I()),
          Seq(A()),
          Seq(A()),
          Seq(A())
        )
        val d1 = ServicioDron.entregarRutaAlmuerzos(dron, ruta)
        assert(d1.last === Left(NumeroAlmuerzosIncorrecto()))
      }
    }
  }
}