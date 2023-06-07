ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.0"

libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.16"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.16" % "test"

lazy val root = (project in file("."))
  .settings(
    name             := "ray-tracing-scala",
    description      := "Ray Tracing implementation in Scala 3",
    version          := "0.1",
    scalaVersion     := "3.3.0",
    idePackagePrefix := Some("com.marantesss.raytracingscala"),
  )
