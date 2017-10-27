package io.newsbridge.sample

import akka.actor.{ActorRef, ActorSystem}
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.stream.ActorMaterializer

import scala.concurrent.Future
import scala.concurrent.duration._

trait DemoServiceSupport {

  def startService(clusterListenerActor: ActorRef, clusterSingletonActor: ActorRef)(implicit system: ActorSystem) = {
    val config = system.settings.config

    val host = config.getString("http.hostname")
    val port = config.getInt("http.port")
    implicit val requestTimeout = Option(Duration(config.getString("http.timeout"))).map(d => FiniteDuration(d.length, d.unit)).get

    implicit val ec = system.dispatcher //bindAndHandle requires an implicit ExecutionContext
    implicit val materializer: ActorMaterializer = ActorMaterializer()

    val api = DemoService(clusterListenerActor, clusterSingletonActor, requestTimeout).routes // the RestApi provides a Route

    val bindingFuture: Future[ServerBinding] = Http().bindAndHandle(api, host, port)

    val log = Logging(system.eventStream, "applications")
    bindingFuture.map { serverBinding =>
      log.info(s"API bound to ${serverBinding.localAddress} ")
    }.failed.foreach {
      case ex: Exception =>
        log.error(ex, "Failed to bind to {}:{}!", host, port)
        system.terminate()
    }
  }
}