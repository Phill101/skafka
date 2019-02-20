import Dependencies._

lazy val commonSettings = Seq(
  organization := "com.evolutiongaming",
  homepage := Some(new URL("http://github.com/evolution-gaming/skafka")),
  startYear := Some(2018),
  organizationName := "Evolution Gaming",
  organizationHomepage := Some(url("http://evolutiongaming.com")),
  bintrayOrganization := Some("evolutiongaming"),
  scalaVersion := crossScalaVersions.value.last,
  crossScalaVersions := Seq("2.11.12", "2.12.8"),
  scalacOptions ++= Seq(
    "-encoding", "UTF-8",
    "-feature",
    "-unchecked",
    "-deprecation",
//    "-Xfatal-warnings",
    "-Xlint",
    "-Yno-adapted-args",
    "-Ywarn-dead-code",
    "-Ywarn-numeric-widen",
    "-Xfuture",
    "-language:higherKinds"),
  scalacOptions in(Compile, doc) ++= Seq("-groups", "-implicits", "-no-link-warnings"),
  resolvers += Resolver.bintrayRepo("evolutiongaming", "maven"),
  licenses := Seq(("MIT", url("https://opensource.org/licenses/MIT"))),
  releaseCrossBuild := true)


lazy val root = (project
  in file(".")
  settings (name := "skafka")
  settings commonSettings
  settings (skip in publish := true)
  aggregate(skafka, logging, prometheus, `play-json`, tests))

lazy val skafka = (project
  in file("skafka")
  settings (name := "skafka")
  settings commonSettings
  settings (libraryDependencies ++= Seq(
    Akka.actor,
    Akka.stream,
    nel,
    `config-tools`,
    Kafka.`kafka-clients`,
    `future-helper`,
    scalatest,
    sequentially,
    `scala-java8-compat`)))

lazy val logging = (project
  in file("modules/logging")
  settings (name := "skafka-logging")
  settings commonSettings
  dependsOn skafka
  settings (libraryDependencies ++= Seq(`safe-actor`)))

lazy val prometheus = (project
  in file("modules/prometheus")
  settings (name := "skafka-prometheus")
  settings commonSettings
  dependsOn skafka
  settings (libraryDependencies ++= Seq(Dependencies.prometheus, `executor-tools`, scalatest)))

lazy val `play-json` = (project
  in file("modules/play-json")
  settings (name := "skafka-play-json")
  settings commonSettings
  dependsOn skafka
  settings (libraryDependencies ++= Seq(Dependencies.`play-json`, scalatest)))

lazy val tests = (project in file("tests")
  settings (name := "skafka-tests")
  settings commonSettings
  settings Seq(
    skip in publish := true,
    Test / fork := true,
    Test / parallelExecution := false)
    dependsOn (skafka, logging)
    settings (libraryDependencies ++= Seq(
      Kafka.kafka % Test,
      `kafka-launcher` % Test,
      Akka.testkit % Test,
      Akka.slf4j % Test,
      Slf4j.api % Test,
      Slf4j.`log4j-over-slf4j` % Test,
      Logback.core % Test,
      Logback.classic % Test,
      scalatest)))