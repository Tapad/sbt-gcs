import sbt._
import sbt.Keys._
import bintray.BintrayKeys._
import sbtrelease.ReleasePlugin.autoImport._
import sbtrelease.ReleaseStateTransformations._

object Publishing {

  val PublishSettings = Seq(
    autoAPIMappings := true,
    bintrayOrganization := Some("tapad-oss"),
    bintrayRepository := "sbt-plugins",
    pomIncludeRepository := { _ => false },
    publishArtifact in Test := false,
    publishArtifact in (Compile, packageDoc) := true,
    publishArtifact in (Compile, packageSrc) := true,
    homepage := Some(new URL("https://github.com/Tapad/sbt-gcs")),
    developers := List(
      Developer(
        id = "pcejrowski",
        name = "Paweł Cejrowski",
        email = "pawel.cejrowski@tapad.com",
        url = url("http://github.com/pcejrowski")
      )
    ),
    scmInfo := Some(
      ScmInfo(
        url("https://github.com/Tapad/sbt-gcs"),
        "scm:git:git://github.com/Tapad/sbt-gcs.git"
      )
    )
  )

  val ReleaseSettings = Seq(
    releaseProcess := Seq[ReleaseStep](
      checkSnapshotDependencies,
      inquireVersions,
      releaseStepCommandAndRemaining("^clean"),
      releaseStepCommandAndRemaining("^test"),
      setReleaseVersion,
      commitReleaseVersion,
      tagRelease,
      releaseStepCommandAndRemaining("^publish"),
      setNextVersion,
      commitNextVersion,
      pushChanges
    )
  )
}