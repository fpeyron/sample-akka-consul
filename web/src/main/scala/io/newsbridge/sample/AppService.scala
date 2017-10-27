package io.newsbridge.demo.producer

import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.{Directives, Route}
import akka.util.Timeout
import main.scala.io.newsbridge.sample.AppMain.logger
import spray.json.DefaultJsonProtocol


object AppService {
}

class AppService(val system: ActorSystem, val timeout: Timeout) extends ShoppersRoutes {
  val executionContext = system.dispatcher
}

trait ShoppersRoutes extends DefaultJsonProtocol with SprayJsonSupport with Directives {

  implicit def timeout: Timeout

  def routes = hello

  val hello: Route = path("hello") {
    get {
      complete {
        logger.info(s"Say Hello to world")
        "Hello world !"
      }
    }
  } ~ path("hello" / Segment) { name =>
    get {
      complete {
        logger.info(s"Say Hello to $name")
        s"Hello $name !"
      }
    }
  }


}

