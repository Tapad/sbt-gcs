import sbt.Keys.organization


val simple = project
  .enablePlugins(GcsPlugin)
  .settings(
    name := "gcs-simple",
    organization := "com.tapad.sbt",
    version := "0.1.0"
  )
  .settings(
    gcsProjectId := "my-google-cloud-project",
      gcsBucket := "gcs-bucket.tapad.com"
  )
  .settings(
    inConfig(IntegrationTest)(
      GcsPlugin.baseSettings ++ Seq(
        gcsProjectId := "my-google-cloud-it-project",
        gcsBucket := "gcs-it-bucket.tapad.com"
      )
    )
  )
  .settings(
    TaskKey[Unit]("check") := {
      assert(
        (gcsArtifactPath in IntegrationTest).value == s"gs://gcs-it-bucket.tapad.com/releases/com/tapad/sbt/gcs-simple/${version.value}/gcs-simple-${version.value}.jar",
        "gcsArtifactPath mismatch"
      )
    }
  )

