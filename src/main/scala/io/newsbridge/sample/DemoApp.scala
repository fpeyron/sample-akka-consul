package io.newsbridge.sample

import akka.actor.{ActorSystem, PoisonPill}
import akka.cluster.Cluster
import akka.cluster.http.management.ClusterHttpManagement
import akka.cluster.singleton.{ClusterSingletonManager, ClusterSingletonManagerSettings, ClusterSingletonProxy, ClusterSingletonProxySettings}
import akka.stream.ActorMaterializer
import com.typesafe.config.{Config, ConfigFactory}

import scala.concurrent.ExecutionContextExecutor

object DemoApp extends App with DemoServiceSupport {


  // configurations
  implicit val config: Config = ConfigFactory.load()

  // Creates the Actor System
  implicit val system: ActorSystem = ActorSystem("sample-akka-http", config)
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  // needed for the future map/flatMap in the end
  implicit val ec: ExecutionContextExecutor = system.dispatcher

  //implicit val requestTimeout: Timeout = Timeout(2.seconds)

  val cluster = Cluster(system)
  ClusterHttpManagement(cluster).start()

  // needed for shutdown properly
  sys.addShutdownHook(system.terminate())

  // Start Actor clusterListener
  val clusterListenerActor = system.actorOf(ClusterListenerActor.props, ClusterListenerActor.Name)


  // Start Actor Singleton
  val clusterSingleton = system.actorOf(
    ClusterSingletonManager.props(
      singletonProps = ClusterSingletonActor.props,
      terminationMessage = PoisonPill,
      settings = ClusterSingletonManagerSettings(system).withRole(None)
    ),
    name = ClusterSingletonActor.Name
  )

  // Start Actor Singleton Proxy
  val clusterSingletonProxy = system.actorOf(
    props = ClusterSingletonProxy.props(
      singletonManagerPath = clusterSingleton.path.toStringWithoutAddress,
      settings = ClusterSingletonProxySettings(system).withRole(None)
    ),
    name = s"${ClusterSingletonActor.Name}Proxy"
  )

  // Start Service Web
  startService(clusterListenerActor, clusterSingletonProxy)

}
