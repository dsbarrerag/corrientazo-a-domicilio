package co.com.pruebaTecnica

import java.util.concurrent.Executors

import co.com.pruebaTecnica.Main.repartirRuta
import co.com.pruebaTecnica.config.Config
import co.com.pruebaTecnica.dao.RepositorioArchivos
import co.com.pruebaTecnica.dron.Dron
import co.com.pruebaTecnica.dron.errores.ErrorDron
import co.com.pruebaTecnica.dron.servicios.ServicioDron
import co.com.pruebaTecnica.ruta.{Movimiento, ServicioRuta}
import com.sun.net.httpserver.Authenticator.Failure

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try

object Main {
  implicit val hilosDrones = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(Config.maxDrones))

  def main(args: Array[String]): Unit = {

  }

  def orquestador(path: String, archivos: Seq[String]) = {
    val entregas: Seq[(String, Int)] = archivos.zip(1 until archivos.size)
    entregas.map {
      case (archivo, id) => Future.fromTry(
        ServicioRuta.traerInstrucciones(path, archivo).map {
          ruta => repartirRuta(ruta, Dron(id))
        }).flatten
        .flatMap(r => Future.fromTry(guardarResultados(path, id, r)))
        .recover {
          case e: IllegalArgumentException => {
            logError(archivo, e)
            Failure[String] // TODO preguntar
          }
        }
    }
  }

  def repartirRuta(instrucciones: Seq[Seq[Movimiento]], dron: Dron): Future[Seq[Either[ErrorDron, Dron]]] =
    Future(ServicioDron.entregarRutaAlmuerzos(dron, instrucciones))

  def logError(archivo: String, exception: Exception) =
    println(s"Caracter Invalido en el archivo ${archivo}, no se ejecutó la ruta")

  def guardarResultados(ruta: String, id: Int, resultados: Seq[Either[ErrorDron, Dron]]): Try[String] =
    RepositorioArchivos.escribirArchivo(ruta, id.toString, resultados.map(_.fold(_.error, _.toString)))

}
