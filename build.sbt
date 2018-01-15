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
maintainer                         in Docker   := "Newsbridge technical support <develop@newsbridge.io>"
dockerBaseImage            := "openjdk:8u141-jre-slim"
//dockerCmd                  := Seq("apt-get update && apt-get install -y iputils-ping")
dockerEntrypoint           := Seq(s"bin/${name.value.toLowerCase}", "-Dconfig.resource=application.conf" )
dockerExposedPorts         := Seq(5000,2550,5010)
dockerUpdateLatest         := true
