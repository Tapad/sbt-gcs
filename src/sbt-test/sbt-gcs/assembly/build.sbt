name := "gcs-assembly"

organization := "com.tapad.sbt"

version := "0.1.0"

assemblyJarName in assembly := s"${name.value}-${version.value}.jar"

gcsProjectId := "my-google-cloud-project"
gcsProjectId in IntegrationTest := "my-google-cloud-it-project"

gcsBucket := "gcs-bucket.tapad.com"
gcsBucket in IntegrationTest  := "gcs-it-bucket.tapad.com"

gcsLocalArtifactPath := (assemblyOutputPath in assembly).value

publish in Gcs := (publish in Gcs).dependsOn(assembly).value

enablePlugins(GcsPlugin)
