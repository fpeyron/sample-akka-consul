package main.scala.io.newsbridge.sample

import akka.actor.ActorSystem
import akka.event.Logging
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory
import io.newsbridge.sample.AppServiceSupport

import scala.concurrent.ExecutionContextExecutor

object AppMain extends App with AppServiceSupport {

  // configurations
  val config = ConfigFactory.parseString(
    s"""
       |akka {
       |  loglevel = INFO
       |  stdout-loglevel = INFO
       |  actor {
       |    provider = "akka.cluster.ClusterActorRefProvider"
       |  }
       |  remote {
       |    log-remote-lifecycle-events = off
       |    netty.tcp {
       |      hostname = "127.0.0.1"
       |      port = 2552
       |    }
       |  }
       |  cluster {
       |    seed-nodes = ["akka.tcp://sample-akka-http@127.0.0.1:2552"]
       |  }
       |  http {
       |    host = "0.0.0.0"
       |    port = 8280
       |    timeout = "2 seconds"
       |  }
       |}
       """.stripMargin).withFallback(ConfigFactory.load())

  // needed to run the route
  implicit val system: ActorSystem = ActorSystem("sample-akka-http", config)
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  // needed for the future map/flatMap in the end
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  // needed for shutdown properly
  sys.addShutdownHook(system.terminate())

  // logger
  val logger = Logging(system, getClass)

  startService()
}
