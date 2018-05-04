package sbtgcs

import sbt.Keys._
import sbt.{Def, _}

object GcsPlugin extends AutoPlugin {

  object autoImport {
    val Gcs = config("gcs").extend(Compile)
    val GcsKeys = sbtgcs.GcsKeys
    val GcsUtils = sbtgcs.GcsUtils
    val gcsLocalArtifactPath = GcsKeys.gcsLocalArtifactPath
    val gcsProjectId = GcsKeys.gcsProjectId
    val gcsBucket = GcsKeys.gcsBucket
    val gcsBlobName = GcsKeys.gcsBlobName
    val gcsOverwrite = GcsKeys.gcsOverwrite
    val gcsArtifactPath = GcsKeys.gcsArtifactPath
  }

  import GcsUtils._
  import autoImport._

  override def projectSettings: Seq[Def.Setting[_]] = defaultSettings ++ requiredSettings ++ inConfig(Gcs)(scopedSettings)

  lazy val defaultSettings = Seq(
    gcsLocalArtifactPath := (artifactPath in (Compile, packageBin)).value,
    gcsBlobName := gcsBlobName.?.value.getOrElse(blobName(organization.value, name.value, version.value)),
    gcsOverwrite := gcsOverwrite.?.value.getOrElse(false),
    gcsArtifactPath := gcsArtifactPath.?.value.getOrElse(s"gs://${gcsBucket.value}/${gcsBlobName.value}")
  )

  lazy val requiredSettings = Seq(
    gcsProjectId := sys.error("The Google Cloud project ID is not defined. Please declare a value for the `gcsProjectId` setting."),
    gcsBucket := sys.error("The Google Cloud Storage bucket is not defined. Please declare a value for the `gcsBucket` setting.")
  )

  lazy val scopedSettings = Seq(
    packageBin := {
      (packageBin in Compile).value
    },
    publish := {
      val _ = (packageBin in Gcs).value
      val localPath = gcsLocalArtifactPath.value
      val remotePath = gcsArtifactPath.value
      val log = streams.value.log
      try {
        upload(log, localPath, gcsProjectId.value, gcsBucket.value, gcsBlobName.value, gcsOverwrite.value)
      } catch {
        case e: Exception =>
          sys.error(s"Could not publish $localPath to $remotePath: " + ErrorHandling.reducedToString(e))
      }
    }
  )
}
