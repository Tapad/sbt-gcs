package sbtgcs

import sbt._
import sbt.Keys._
import java.io.File

object GcsKeys {
  val gcsLocalArtifactPath = taskKey[File]("The location of the local resource that will be published to GCS")
  val gcsProjectId = settingKey[String]("The Google Cloud Storage project ID")
  val gcsBucket = settingKey[String]("The Google Cloud Storage bucket")
  val gcsBlobName = settingKey[String]("The Google Cloud Storage artifact BLOB name")
  val gcsOverwrite = settingKey[Boolean]("Specifies whether to overwrite BLOB if exists, default: false")
  val gcsMimeType = settingKey[String]("Specifies the MIME type of the uploaded file, default: application/java-archive")
  val gcsArtifactPath = settingKey[String]("The GCS path where an artifact will be published")
}
