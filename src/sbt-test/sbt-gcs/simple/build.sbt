name := "gcs-simple"

organization := "com.tapad.sbt"

version := "0.1.0"

gcsProjectId := "my-google-cloud-project"

gcsBucket := "gcs-bucket.tapad.com"

enablePlugins(GcsPlugin)