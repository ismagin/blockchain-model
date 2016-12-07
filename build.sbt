name := "zero-scala"

version := "1.0"

scalaVersion := "2.12.0"

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"
resolvers += Resolver.sonatypeRepo("releases")

addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.3")

scalacOptions := Seq("-Ypartial-unification", "-feature", "-language:higherKinds") //if running 2.12

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.0"
libraryDependencies += "org.typelevel" %% "cats" % "0.8.1"