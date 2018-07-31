package co.com.pruebaTecnica.dron

import co.com.pruebaTecnica.parsers.ServicioInstrucciones
import org.scalatest.FunSpec

class InstruccionesSpecs extends FunSpec {
  describe("El servicio de instrucciones") {
    describe("Dado una ruta a un archivo") {
      val path = "/home/s4n/Downloads/test.txt"
      it("Deberia retornar las lineas del archivo como un arreglo") {
        val res = ServicioInstrucciones.leerArchivo(path)
        res.recover {
          case e: Exception => println(e.getMessage)
        }
        res.map(l => println(l))
        assert(res.isSuccess)
      }
    }

    describe("Dado un nombre de archivo y una secuencia de Strings") {
      val archivo = "/home/s4n/Downloads/testOut.txt"
      val secuencia = Seq("Hola", "Prueba", "123")
      it("Deberia guardar un archivo") {
        ServicioInstrucciones.escribirArchivo(archivo, secuencia)
        assert(true)
      }
    }

  }
}