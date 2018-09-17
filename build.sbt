name := "valentiay-crud"

version := "0.1"

scalaVersion := "2.12.6"

lazy val dependencies = Seq(
  "com.github.finagle" %% "finch-core" % "0.22.0",
  "com.github.finagle" %% "finch-circe" % "0.22.0",
  "io.monix" %% "monix" % "3.0.0-RC1",
  "com.propensive" %% "magnolia" % "0.7.1",
  "io.circe" %% "circe-generic" % "0.9.3",
  "com.typesafe" % "config" % "1.3.2",
)

lazy val root = project in file(".") dependsOn core aggregate core
lazy val core = project in file("core") settings (libraryDependencies ++= dependencies)

resolvers += Resolver.sonatypeRepo("releases")
addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)
