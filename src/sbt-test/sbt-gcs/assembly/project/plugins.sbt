addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.15.0")

{
  sys.props.get("plugin.version") match {
    case Some(pluginVersion) =>
      addSbtPlugin("com.tapad.sbt" % "sbt-gcs" % pluginVersion)
    case None =>
      sys.error(
        """
          |The system property 'plugin.version' is not defined.
          |Specify this property using the scriptedLaunchOpts -D.
        """.stripMargin.trim
      )
  }
}
