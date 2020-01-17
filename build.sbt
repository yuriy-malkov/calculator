name := "calculator"

version := "0.1"

scalaVersion := "2.13.1"

val akkaVersion = "2.6.1"

libraryDependencies += "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion
libraryDependencies += "com.typesafe.akka" %% "akka-actor-testkit-typed" % akkaVersion % Test
libraryDependencies += "org.scalatest" % "scalatest_2.13" % "3.1.0" % "test"
