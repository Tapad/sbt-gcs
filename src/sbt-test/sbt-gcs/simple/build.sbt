name := "gcs-simple"

organization := "com.tapad.sbt"

version := "0.1.0"

gcsProjectId := "my-google-cloud-project"
gcsProjectId in IntegrationTest := "my-google-cloud-it-project"

gcsBucket := "gcs-bucket.tapad.com"
gcsBucket in IntegrationTest := "gcs-it-bucket.tapad.com"

enablePlugins(GcsPlugin)

TaskKey[Unit]("check") := {

  assert(
    (gcsArtifactPath in Gcs).value == s"gs://gcs-bucket.tapad.com/releases/com/tapad/sbt/gcs-simple/${version.value}/gcs-simple-${version.value}.jar",
    "gcsArtifactPath  mismatch"
  )

  assert(
    (gcsArtifactPath in IntegrationTest).value == s"gs://gcs-it-bucket.tapad.com/releases/com/tapad/sbt/gcs-simple/${version.value}/gcs-simple-${version.value}.jar",
    "gcsArtifactPath  mismatch"
  )

}