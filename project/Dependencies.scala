  /*_*/
object Dependencies {

    object versions {
      val finch = "0.22.0"
      val monix = "3.0.0-RC1"
      val magnolia = "0.7.1"
      val circe = "0.9.3"
      val config = "1.3.2"
      val logback = "1.2.3"
      val logging = "3.9.0"
    }

    object libs {
      val finchCore = "com.github.finagle" %% "finch-core"
      val finchCirce = "com.github.finagle" %% "finch-circe"
      val monix = "io.monix" %% "monix"
      val magnolia = "com.propensive" %% "magnolia"
      val circe = "io.circe" %% "circe-generic"
      val config = "com.typesafe" % "config"
      val logback = "ch.qos.logback" % "logback-classic"
      val logging = "com.typesafe.scala-logging" %% "scala-logging"
    }

    val artifacts = Seq(
      libs.finchCore % versions.finch,
      libs.finchCirce % versions.finch,
      libs.monix % versions.monix,
      libs.magnolia % versions.magnolia,
      libs.circe % versions.circe,
      libs.config % versions.config,
      libs.logback % versions.logback,
      libs.logging % versions.logging,
    )
}
