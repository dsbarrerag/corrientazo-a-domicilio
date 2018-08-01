package co.com.pruebaTecnica.dao

import co.com.pruebaTecnica.ruta.ServicioRuta
import org.scalatest.FunSpec

class InstruccionesSpecs extends FunSpec {
  describe("El servicio de instrucciones") {
    describe("Dado un nombre de archivo y una secuencia de Drones") {
      val archivo = "/home/s4n/Downloads/testOut.txt"
      val secuencia = Seq("Hola", "Prueba", "123")
      it("Deberia guardar un archivo") {
        //ServicioInstrucciones.escribirArchivo(archivo, secuencia)
       // assert(true)
      }
    }

  }
}