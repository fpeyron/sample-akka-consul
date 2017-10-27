package io.newsbridge.sample

import akka.actor.{Actor, ActorLogging, Props}
import com.typesafe.config.{Config, ConfigFactory}


object ClusterSingletonActor {

  final val Name = "clusterSingleton"

  def props: Props = Props[ClusterSingletonActor]


}

class ClusterSingletonActor extends Actor with ActorLogging {

  implicit val config: Config = ConfigFactory.load()
  val host = config.getString("akka.remote.netty.tcp.hostname")
  val port = config.getInt("akka.remote.netty.tcp.port")


  override def receive = {
    case _  =>
      sender() ! s"$host:$port"
  }
}
