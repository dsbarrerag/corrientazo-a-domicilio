package co.com.pruebaTecnica.dron

import co.com.pruebaTecnica.movimiento.{A, D, I, Movimiento}
import co.com.pruebaTecnica.parsers.ServicioInstrucciones
import org.scalatest.FunSpec

class InstruccionesSpecs extends FunSpec {
  describe("El servicio de instrucciones") {

    describe("dado un caracter") {
      it("deberia convertir A en la instruccion A") {
        val i: Char = 'A'
        assert(ServicioInstrucciones.parseLetra(i) === A)
      }
      it("deberia convertir I en la instruccion I") {
        val i: Char = 'I'
        assert(ServicioInstrucciones.parseLetra(i) === I)
      }
      it("deberia convertir D en la instruccion D") {
        val i: Char = 'D'
        assert(ServicioInstrucciones.parseLetra(i) === D)
      }
    }
    describe("dada una linea de Strings") {
      it("Deberia convertirla en un arreglo de instrucciones"){
        val linea = "AAAAIAAD"
        assertResult(ServicioInstrucciones.parseLinea(linea)){
          Seq(A,A,A,A,I,A,A,D)
        }
      }
    }
  }
}