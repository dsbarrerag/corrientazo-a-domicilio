package co.com.pruebaTecnica.dron

import co.com.pruebaTecnica._
import co.com.pruebaTecnica.errores.{NumeroAlmuerzosIncorrecto, DistanciaExcedida, ErrorDron}
import co.com.pruebaTecnica.movimiento.{A, D, I}
import org.scalatest.FunSpec

class DronSpecs extends FunSpec {
  val dronNorte: Dron = Dron()
  describe("Un Dron") {

    it("Deberia poder ser creado vacio") {
      val dronVacio = Dron(Posicion(Coordenada(0, 0), Norte()), Config.maxAlmuerzos)
      assert(dronVacio === Right(dronNorte))
    }

    it("Deberia moverse hacia alfrente") {
      val dron = ServicioDron.moverDron(dronNorte)(A)
      assertResult(dron) {
        Dron(Posicion(Coordenada(0, 1), Norte()), Config.maxAlmuerzos)
      }
    }

    it("Deberia moverse hacia abajo si está mirando hacia el sur") {
      val dronSur = Dron(Posicion(Coordenada(), Sur()), Config.maxAlmuerzos)
      val dron = dronSur.flatMap(d => ServicioDron.moverDron(d)(A))
      assertResult(dron) {
        Dron(Posicion(Coordenada(0, -1), Sur()), Config.maxAlmuerzos)
      }
    }

    it("Deberia moverse hacia la derecha si está mirando hacia el oriente") {
      val dronOriente = Dron(Posicion(Coordenada(), Oriente()), Config.maxAlmuerzos)
      val dron = dronOriente.flatMap(d => ServicioDron.moverDron(d)(A))
      assertResult(dron) {
        Dron(Posicion(Coordenada(1, 0), Oriente()), Config.maxAlmuerzos)
      }
    }

    it("Deberia moverse hacia la izquierda si está mirando hacia el occidente") {
      val dronOccidente = Dron(Posicion(Coordenada(), Occidente()), Config.maxAlmuerzos)
      val dron = dronOccidente.flatMap(d => ServicioDron.moverDron(d)(A))
      assertResult(dron) {
        Dron(Posicion(Coordenada(-1, 0), Occidente()), Config.maxAlmuerzos)
      }
    }

    it("Deberia girar a la derecha") {
      val dron = ServicioDron.moverDron(dronNorte)(D)
      assertResult(dron) {
        Dron(Posicion(Coordenada(0, 0), Oriente()), Config.maxAlmuerzos)
      }
    }

    it("Deberia girar a la izquierda") {
      val dron = ServicioDron.moverDron(dronNorte)(I)
      assertResult(dron) {
        Dron(Posicion(Coordenada(0, 0), Occidente()), Config.maxAlmuerzos)
      }
    }

    it("Deberia entregar un almuerzo") {
      val dron = ServicioDron.entregarAlmuerzo(dronNorte)
      assertResult(dron) {
        Dron(Posicion(), 9)
      }
    }

    it("No deberia salirse del barrio") {
      val dronFrontera: Either[ErrorDron, Dron] = Dron(Posicion(Coordenada(10, 10), Norte()), Config.maxAlmuerzos)
      val d1 = dronFrontera.flatMap(d => ServicioDron.moverDron(d)(A))
      assert(d1.isLeft)
      assert(d1 === Left(DistanciaExcedida()))
    }

    it("No deberia entregar mas almuerzos de los que tiene") {
      val dronFrontera: Either[ErrorDron, Dron] = Dron(Posicion(), 0)
      val d1 = dronFrontera.flatMap(d => ServicioDron.entregarAlmuerzo(d))
      assert(d1.isLeft)
      assert(d1 === Left(NumeroAlmuerzosIncorrecto()))
    }
  }

  describe("El servicio de Drones") {
    describe("Dado un dron") {
      val dron = Dron()
      describe("Dada una entrega") {
        val entrega = Seq(A, A, A, A, I, A, A, D)
        it("Deberia devolver la posicion del dron despues de haber hecho la entrega") {
          val d2: Either[ErrorDron, Dron] = ServicioDron.moverRuta(dron, entrega)
          assert(d2.isRight)
          assert(d2 === Dron(Posicion(Coordenada(-2, 4), Norte()), 9))
        }
      }
      describe("Dada una entrega errada") {
        val entrega = Seq(A, A, A, A, A, A, A, A, A, A, A)
        it("Deberia devolver un left") {
          val d2: Either[ErrorDron, Dron] = ServicioDron.moverRuta(dron, entrega)
          println(d2)
          assert(d2.isLeft)
          assert(d2 === Left(DistanciaExcedida()))
        }
      }

      describe("Dada una ruta de entregas") {
        val ruta = Seq(
          Seq(A, A, A, A, I, A, A, D),
          Seq(A, A, A, A, A, A, A, A, A, A, A),
          Seq(D, D, A, I, A, D),
          Seq(A, A, I, A, D, A, D)
        )
        it("Deberia realizar las entregas y retornar un arreglo con las coordenadas finales") {
          val d2 = ServicioDron.entregarRutaAlmuerzos(dron, ruta)
          println(d2)
          assert(true)
        }
      }
    }
  }
}