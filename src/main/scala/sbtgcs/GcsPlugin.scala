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
    val gcsMimeType = GcsKeys.gcsMimeType
    val gcsArtifactPath = GcsKeys.gcsArtifactPath
  }

  import GcsUtils._
  import autoImport._

  val configurations: Seq[Configuration] = Seq(Gcs, IntegrationTest, Test)

  override def projectSettings: Seq[Def.Setting[_]] = defaultSettings ++ configurations.flatMap(inConfig(_)(scopedSettings))

  lazy val defaultSettings = Seq(
    gcsLocalArtifactPath := (artifactPath in (Compile, packageBin)).value,
    gcsBlobName := gcsBlobName.?.value.getOrElse(blobName(organization.value, name.value, version.value)),
    gcsOverwrite := gcsOverwrite.?.value.getOrElse(false),
    gcsMimeType := gcsMimeType.?.value.getOrElse("application/java-archive")
  )

  lazy val scopedSettings = Seq(
    gcsProjectId := gcsProjectId.?.value.getOrElse(sys.error("The Google Cloud project ID is not defined. Please declare a value for the `gcsProjectId` setting.")),
    gcsBucket := gcsBucket.?.value.getOrElse(sys.error("The Google Cloud Storage bucket is not defined. Please declare a value for the `gcsBucket` setting.")),
    gcsArtifactPath := gcsArtifactPath.?.value.getOrElse(s"gs://${gcsBucket.value}/${gcsBlobName.value}"),
    packageBin := {
      (packageBin in Compile).value
    },
    publish := {
      val _ = (packageBin in Gcs).value
      val source = gcsLocalArtifactPath.value
      val destination = gcsArtifactPath.value
      val log = streams.value.log
      try {
        upload(log, gcsProjectId.value, source, destination, gcsOverwrite.value, gcsMimeType.value)
      } catch {
        case e: Exception =>
          sys.error(s"Could not publish $source to $destination: " + ErrorHandling.reducedToString(e))
      }
    }
  )
}
