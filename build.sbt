name := "valentiay-crud"

version := "0.1"

scalaVersion := "2.12.6"

lazy val dependencies = Dependencies.artifacts
lazy val root = project in file(".") dependsOn core aggregate core
lazy val core = project in file("core") settings (libraryDependencies ++= dependencies)

resolvers += Resolver.sonatypeRepo("releases")
addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)
