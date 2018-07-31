package co.com.pruebaTecnica

import java.util.concurrent.Executors

import co.com.pruebaTecnica.dron.Dron
import co.com.pruebaTecnica.movimiento.Movimiento

import scala.concurrent.{ExecutionContext, Future}

object Main {
  implicit val hilosDrones = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(20))

  def main(args: Array[String]): Unit = {

  }

  def repartirRuta(instrucciones: Seq[Seq[Movimiento]], dron: Dron): Future[Seq[String]] =
  Future (ServicioDron.entregarRutaAlmuerzos(dron, instrucciones))



}
