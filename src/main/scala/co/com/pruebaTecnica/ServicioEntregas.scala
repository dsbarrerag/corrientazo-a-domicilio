package co.com.pruebaTecnica

import java.util.concurrent.Executors

import co.com.pruebaTecnica.config.Config
import co.com.pruebaTecnica.dao.RepositorioArchivos
import co.com.pruebaTecnica.dron.Dron
import co.com.pruebaTecnica.dron.errores.ErrorDron
import co.com.pruebaTecnica.dron.servicios.ServicioDron
import co.com.pruebaTecnica.ruta.{Movimiento, ServicioRuta}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try

sealed trait ServicioEntregasAlgebra {
  type nombreArchivoSalida = String

  def entregarAlmuerzos(ruta: String, nombreArchivos: Seq[String]): Seq[Future[nombreArchivoSalida]]

}

sealed trait ServicioEntregas extends ServicioEntregasAlgebra {
  implicit val hilosDrones = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(Config.maxDrones))

  override def entregarAlmuerzos(ruta: String, nombreArchivos: Seq[String]): Seq[Future[nombreArchivoSalida]] = {
    val entregas: Seq[(String, Int)] = nombreArchivos.zip(1 until nombreArchivos.size + 1)
    entregas.map { case (archivo, id) =>
      for {
        rutaEntrega <- Future.fromTry(ServicioRuta.traerInstrucciones(ruta, archivo))
        posiciones <- repartirRuta(rutaEntrega, Dron(id))
        archivoSalida <- Future.fromTry(guardarResultados(ruta, id, posiciones))
      } yield archivoSalida
    }
  }

  private def repartirRuta(instrucciones: Seq[Seq[Movimiento]], dron: Dron): Future[Seq[Either[ErrorDron, Dron]]] =
    Future(ServicioDron.entregarRutaAlmuerzos(dron, instrucciones))

  private def guardarResultados(ruta: String, id: Int, resultados: Seq[Either[ErrorDron, Dron]]): Try[String] = {
    val nombre = f"${id}%02d"
    RepositorioArchivos.escribirArchivo(ruta, nombre, resultados.map(_.fold(_.error, _.toString)))
  }

  private def logExcepcion(archivo: String, exception: Exception) =
    println(s"Caracter Invalido en el archivo ${archivo}, no se ejecutó la ruta")

  private def generarNombreArchivosEntrada(numero: Int): Seq[String] =
    (1 until numero + 1).map(id => s"in${f"${id}%02d"}.txt")
}

object ServicioEntregas extends ServicioEntregas