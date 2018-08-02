package co.com.pruebaTecnica

import co.com.pruebaTecnica.dao.RepositorioArchivos
import org.scalatest.FunSpec

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}


class ServicioEntregasSpecs extends FunSpec {

  def nombreArchivos(numero: Int): Seq[String] = (1 until numero +1).map(id => s"in${f"${id}%02d"}.txt")

  describe("Dado una lista de nombres de archivos y una ruta") {
    val numArchivos = 5
    val archivos = nombreArchivos(numArchivos)
    val ruta = "src/test/resources/loteBueno/"
    describe("Deberia procesar los pedidos y generar una lista de nombres de archivos de salida") {
      val nombresSalida: Seq[String] = Await.result(Future.sequence(ServicioEntregas.entregarAlmuerzos(ruta, archivos)), 3 seconds)
      it("Deberia generar archivos out.txt en el mismo directorio con las direcciones de entrega correctas") {
        val archivosSalida = nombresSalida.map(n => RepositorioArchivos.leerArchivo(ruta, n))
        val archivosPrueba = (1 until numArchivos).map(id =>
          RepositorioArchivos.leerArchivo(ruta, s"outBueno${f"${id}%02d"}.txt")
        )
        archivosPrueba.zip(archivosSalida).map(t => assert(t._1 === t._2))
      }
    }
  }

  describe("Dado un lote de archivos en el que un archivo contenga un caracter invalido") {
    val numArchivos = 3
    val archivos = nombreArchivos(numArchivos)
    val ruta = "src/test/resources/loteCaracterInvalido/"
    describe("Deberia procesar los pedidos y generar una lista de nombres de archivos de salida") {
      val nombresSalida = ServicioEntregas.entregarAlmuerzos(ruta, archivos)
      it("La secuencia deberia tener un futuro fallido") {
        assertThrows[IllegalArgumentException](Await.result(Future.sequence(nombresSalida), 2 seconds))
      }
      it("Deberia generar archivos out.txt en el mismo directorio con las direcciones de entrega correctas") {
        val salidasFuturo = nombresSalida.map(_.recover{
          case e: IllegalArgumentException => "Caracter Invalido"
        })
        val salidas = Await.result(Future.sequence(salidasFuturo), 3 seconds)
          .filterNot(_.contains("Caracter Invalido"))
        val archivosSalida = salidas.map(n => RepositorioArchivos.leerArchivo(ruta, n))
        val archivosPrueba = Seq(1,3).map(id =>
          RepositorioArchivos.leerArchivo(ruta, s"outBueno${f"${id}%02d"}.txt")
        )
        archivosPrueba.zip(archivosSalida).map(t => assert(t._1 === t._2))
      }
    }
  }
}

