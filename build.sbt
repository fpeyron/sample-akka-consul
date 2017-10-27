import sbt.Keys.dependencyOverrides

organization := "io.newsbridge.io.newsbridge.sample"
name := "io.newsbridge.sample-akka-consul"
version := "1.0.0"

lazy val akkaVersion = "2.5.4"
lazy val akkaHttpVersion = "10.0.9"



lazy val commonDependencies = Seq(
  "com.typesafe.akka" %% "akka-http"              % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-parsing"           % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-spray-json"   % akkaHttpVersion,
  // ----------------
  "com.typesafe.akka" %% "akka-actor"             % akkaVersion,
  "com.typesafe.akka" %% "akka-stream"            % akkaVersion,
  "com.typesafe.akka" %% "akka-slf4j"             % akkaVersion,
  // ----------------
  "com.typesafe.akka" %% "akka-stream"            % akkaVersion,
  "com.typesafe.akka" %% "akka-actor"             % akkaVersion,
  // ----------------
  "com.typesafe.akka" %% "akka-cluster"           % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster-metrics"   % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster-sharding"  % akkaVersion,
  "com.typesafe.akka" %% "akka-remote"            % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster-tools"     % akkaVersion,
  // ----------------
  "com.orbitz.consul" % "consul-client"           % "0.13.3"
)


lazy val commonDependencyOverrides = Set(
  "com.typesafe.akka"  %% "akka-actor"   % akkaVersion,
  "com.typesafe.akka"  %% "akka-stream"  % akkaVersion,
  "com.typesafe.akka"  %% "akka-cluster" % akkaVersion
)



lazy val sampleWeb = project.in(file("web"))
  .settings(
    name := "sampleWeb",
    scalaVersion := "2.12.3",
    libraryDependencies := commonDependencies,
    dependencyOverrides:= commonDependencyOverrides
)
/*
dependencyOverrides += "com.typesafe.akka"  %% "akka-actor"   % akkaVersion,
    dependencyOverrides += "com.typesafe.akka"  %% "akka-stream"  % akkaVersion,
    dependencyOverrides += "com.typesafe.akka"  %% "akka-cluster" % akkaVersion

 */

