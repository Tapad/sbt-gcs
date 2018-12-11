package sbtgcs

import sbt.Keys.{publish, _}
import sbt.{Def, _}

object GcsPlugin extends AutoPlugin {

  object autoImport {
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

  override def projectSettings: Seq[Def.Setting[_]] = baseSettings

  lazy val baseSettings = defaultSettings ++ dependentSettings ++ publishSettings

  lazy val dependentSettings = Seq(
    gcsLocalArtifactPath := (artifactPath in(Compile, packageBin)).value,
    gcsArtifactPath  := s"gs://${gcsBucket.value}/${gcsBlobName.value}"
  )

  lazy val publishSettings = Seq(
    packageBin := (packageBin in Compile).value,
    publish := {
      val _ = packageBin.value
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

  lazy val defaultSettings = Seq(
    gcsBlobName := blobName(organization.value, name.value, version.value),
    gcsOverwrite := false,
    gcsMimeType := "application/java-archive"
  )

}
