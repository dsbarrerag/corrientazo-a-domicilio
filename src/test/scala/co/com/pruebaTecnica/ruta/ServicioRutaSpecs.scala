package co.com.pruebaTecnica.ruta

import org.scalatest.FunSpec

class ServicioRutaSpecs extends FunSpec {
  describe("El servicio de instrucciones") {
    describe("Dado una ruta y un nombre de archivo") {
      val ruta = "src/test/resources/loteBueno/"
      val archivo = "in01.txt"
      it("deberia cargar el archivo y devolver una ruta de entregas"){
        {
          val entrega = ServicioRuta.traerInstrucciones(ruta, archivo)
          val res = Seq(
              Seq(A(),A(),A(),A(),I(),A(),A(),D()),
              Seq(D(),D(),A(),I(),A(),D()),
              Seq(A(),A(),I(),A(),D(),A(),D()),
              Seq(A(),A(),A(),D(),A(),I(),A())
          )
          assert(entrega.getOrElse(Seq.empty[Seq[Movimiento]]) === res)
        }
      }
    }

    describe("Dado un archivo que no exista"){
      val ruta = "src/test/resources/loteBueno/"
      val archivo = "inexistente.txt"
      it("Deberia devolver un try fallido"){
        val entrega = ServicioRuta.traerInstrucciones(ruta, archivo)
        assert(entrega.isFailure)
      }
    }

    describe("Dado un archivo con un caracter invalido"){
      val ruta = "src/test/resources/loteBueno/"
      val archivo = "errorOnInput.txt"
      it("Deberia devolver un try fallido"){
        val entrega = ServicioRuta.traerInstrucciones(ruta, archivo)
        assert(entrega.isFailure)
      }
    }

    describe("Dado un archivo vacio"){
      val ruta = "src/test/resources/loteBueno/"
      val archivo = "archivoVacio.txt"
      it("Deberia devolver un try fallido"){
        val entrega = ServicioRuta.traerInstrucciones(ruta, archivo)
        assert(entrega.isFailure)
      }
    }
  }
}