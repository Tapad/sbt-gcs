name := "gcs-simple"

organization := "com.tapad.sbt"

version := "0.1.0"

gcsProjectId in Gcs := "my-google-cloud-project"
gcsProjectId in IntegrationTest := "my-google-cloud-it-project"

gcsBucket in Gcs := "gcs-bucket.tapad.com"
gcsBucket in IntegrationTest := "gcs-it-bucket.tapad.com"

enablePlugins(GcsPlugin)