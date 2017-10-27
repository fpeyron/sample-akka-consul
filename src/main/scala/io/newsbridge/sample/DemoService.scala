package io.newsbridge.sample

import akka.actor.{ActorRef, ActorSystem, Address}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.{Directives, Route}
import akka.util.Timeout
import spray.json.DefaultJsonProtocol

import scala.concurrent.ExecutionContextExecutor


object DemoService {

}

case class DemoService(clusterListenerActor: ActorRef, clusterSingletonProxy: ActorRef, requestTimeout: Timeout)
                      (implicit val system: ActorSystem) extends DemoRoutes {
  val executionContext: ExecutionContextExecutor = system.dispatcher
}

trait DemoRoutes extends DefaultJsonProtocol with SprayJsonSupport with Directives {

  import akka.pattern.ask

  def clusterListenerActor: ActorRef

  def clusterSingletonProxy: ActorRef

  implicit def requestTimeout: Timeout

  def routes: Route = clusterListener ~ clusterSingleton


  val clusterListener: Route = path("member-nodes") { // List cluster nodes
    get {
      onSuccess(
        (clusterListenerActor ? ClusterListenerActor.GetMemberNodes).mapTo[Set[Address]]
      )(addresses =>
        complete(addresses.map(_.toString)))
    }
  }


  val clusterSingleton: Route = path("who-is-the-boss") { // Get singleton
    get {
      onSuccess((
        clusterSingletonProxy ? "").mapTo[String]
      ) { msg =>
        complete(msg)
      }
    }
  }
}

