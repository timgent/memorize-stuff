name := """memorize-stuff"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  ws,
  "org.webjars" %% "webjars-play" % "2.3.0-2",
  "org.webjars" % "bootstrap" % "3.1.1-2",
  "org.scalatestplus" %% "play" % "1.2.0" % "test",
  "org.reactivemongo" %% "play2-reactivemongo" % "0.10.5.0.akka23"
)