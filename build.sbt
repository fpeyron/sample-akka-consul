organization := "io.newsbridge.io.newsbridge.sample"
name := "sample-akka-consul"

lazy val akkaVersion      = "2.5.8"
lazy val akkaHttpVersion  = "10.0.11"
lazy val ConstructrAkka   = "0.18.1"

resolvers += Resolver.bintrayRepo("everpeace", "maven")

libraryDependencies := Seq(
  "com.typesafe.akka"   %% "akka-http"                      % akkaHttpVersion,
  "com.typesafe.akka"   %% "akka-parsing"                   % akkaHttpVersion,
  "com.typesafe.akka"   %% "akka-http-spray-json"           % akkaHttpVersion,
  // ----------------
  "com.typesafe.akka"   %% "akka-actor"                     % akkaVersion,
  "com.typesafe.akka"   %% "akka-slf4j"                     % akkaVersion,
  "com.typesafe.akka"   %% "akka-stream"                    % akkaVersion,
  // ----------------
  "com.typesafe.akka"   %% "akka-cluster"                   % akkaVersion,
  "com.typesafe.akka"   %% "akka-cluster-metrics"           % akkaVersion,
  "com.typesafe.akka"   %% "akka-cluster-sharding"          % akkaVersion,
  "com.typesafe.akka"   %% "akka-remote"                    % akkaVersion,
  "com.typesafe.akka"   %% "akka-cluster-tools"             % akkaVersion,
  // ----------------
  "de.heikoseeberger"   %% "constructr"                     % ConstructrAkka,
  "com.github.everpeace" %% "constructr-coordination-redis" % "0.0.4",
  // ----------------
  "com.lightbend.akka"  %% "akka-management-cluster-http"   % "0.5"
)


mainClass in (Compile, run) := Some("io.newsbridge.sample.DemoApp")

enablePlugins(DockerPlugin, JavaAppPackaging)

NativePackagerKeys.packageName     in Docker   := name.value
maintainer                         in Docker   := "technical support <florent.peyron@gmail.com>"
dockerBaseImage            := "openjdk:8u151-jre" //-slim"
dockerCmd                  := Seq("apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*")
//dockerCmd                  := Seq("apk add --no-cache curl")
dockerEntrypoint           := Seq("sh","-c","HOST_IP=$(/usr/bin/curl -s --connect-timeout 1 169.254.169.254/latest/meta-data/local-ipv4) && SERVICE_AKKA_HOST=$HOST_IP ; echo SERVICE_AKKA_HOST:$SERVICE_AKKA_HOST ; " + s"bin/${name.value.toLowerCase} -Dconfig.resource=application.conf" + " -Dakka.remote.netty.tcp.hostname=$SERVICE_AKKA_HOST")
//dockerEntrypoint           := Seq("sh","-c", s"bin/${name.value.toLowerCase}", "-Dconfig.resource=application.conf", "-Dakka.remote.netty.tcp.bind-hostname=$(/usr/bin/curl -s --connect-timeout 1 169.254.169.254/latest/meta-data/local-ipv4)", "-dakka.remote.netty.tcp.hostname=$(/usr/bin/curl -s --connect-timeout 1 169.254.169.254/latest/meta-data/local-ipv4)")
dockerExposedPorts         := Seq(5000,2550,5010)
dockerUpdateLatest         := true

//dockerBuildCommand    := Seq("apt-get update && apt-get install -y apt install curl")
//dockerCmd                  := Seq("apt-get install -y curl")//dockerEntrypoint           := Seq("SERVICE_AKKA_HOST=$(usr/bin/curl -s --connect-timeout 1 http://169.254.169.25^4/latest/meta-data/local-ipv4)  &&", s"bin/${name.value.toLowerCase}", "-Dconfig.resource=application.conf" )
//dockerEntrypoint           := Seq(s"bin/${name.value.toLowerCase}", "-Dconfig.resource=application.conf", "-Dakka.remote.bind-hostname=$(usr/bin/curl -s --connect-timeout 1 http://169.254.169.25^4/latest/meta-data/local-ipv4)", "-dakka.remote.hostname=$(usr/bin/curl -s --connect-timeout 1 http://169.254.169.25^4/latest/meta-data/local-ipv4)" )

//dockerEntrypoint           := Seq(s"bin/${name.value.toLowerCase}", "${JAVA_OPTS} -Dconfig.resource=application.conf")