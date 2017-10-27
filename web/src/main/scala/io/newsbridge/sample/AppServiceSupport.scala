package io.newsbridge.sample

import akka.actor.{ActorRef, ActorSystem}
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.typesafe.config.Config
import io.newsbridge.demo.producer.AppService

import scala.concurrent.Future

trait AppServiceSupport extends RequestTimeout {

  def startService()(implicit system: ActorSystem) = {
    val config = system.settings.config

    val host = config.getString("akka.http.host")
    val port = config.getInt("akka.http.port")

    implicit val ec = system.dispatcher  //bindAndHandle requires an implicit ExecutionContext

    val api = new AppService(system, requestTimeout(config)).routes // the RestApi provides a Route

    implicit val materializer = ActorMaterializer()
    val bindingFuture: Future[ServerBinding] =
      Http().bindAndHandle(api, host, port)

    val log =  Logging(system.eventStream, "applications")
    bindingFuture.map { serverBinding =>
      log.info(s"API bound to ${serverBinding.localAddress} ")
    }.failed.foreach {
      case ex: Exception =>
        log.error(ex, "Failed to bind to {}:{}!", host, port)
        system.terminate()
    }
  }
}

trait RequestTimeout {
  import scala.concurrent.duration._
  def requestTimeout(config: Config): Timeout = {
    val t = config.getString("akka.http.timeout")
    val d = Duration(t)
    FiniteDuration(d.length, d.unit)
  }
}
