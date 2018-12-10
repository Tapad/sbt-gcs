name := "gcs-assembly"

organization := "com.tapad.sbt"

version := "0.1.0"

enablePlugins(GcsPlugin)

assemblyJarName in assembly := s"${name.value}-${version.value}.jar"

gcsProjectId := "my-google-cloud-project"

gcsBucket := "gcs-bucket.tapad.com"

inConfig(IntegrationTest)(
  GcsPlugin.baseSettings ++ Seq(
    gcsBucket := "gcs-it-bucket.tapad.com"
  )
)

gcsLocalArtifactPath := (assemblyOutputPath in assembly).value

publish := publish.dependsOn(assembly).value

TaskKey[Unit]("check") := {

  assert(
    gcsArtifactPath.value == s"gs://gcs-bucket.tapad.com/releases/com/tapad/sbt/gcs-simple/${version.value}/gcs-simple-${version.value}.jar",
    "gcsArtifactPath  mismatch"
  )

  assert(
    gcsProjectId.value == "my-google-cloud-project", "gcsProjectId  mismatch"
  )
  assert(
    (gcsProjectId in IntegrationTest).value == "my-google-cloud-project", "gcsProjectId  mismatch"
  )

  assert(
    (gcsArtifactPath in IntegrationTest).value == s"gs://gcs-it-bucket.tapad.com/releases/com/tapad/sbt/gcs-simple/${version.value}/gcs-simple-${version.value}.jar",
    "gcsArtifactPath  mismatch"
  )

}