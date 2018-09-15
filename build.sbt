name := "valentiay-crud"

version := "0.1"

scalaVersion := "2.12.6"

lazy val dependencies = Seq(
  "com.github.finagle" %% "finch-core" % "0.22.0",
  "com.github.finagle" %% "finch-circe" % "0.22.0",
  "io.monix" %% "monix" % "3.0.0-RC1",
  "io.circe" %% "circe-generic" % "0.9.3",
)

lazy val root = project in file(".") dependsOn core aggregate core
lazy val core = project in file("core") settings (libraryDependencies ++= dependencies)