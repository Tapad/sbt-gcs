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
    pomExtra := {
      <developers>
        <developer>
          <id>pcejrowski</id>
          <name>Pawel Cejrowski</name>
          <email>pawel.cejrowski@tapad.com</email>
          <url>https://github.com/pcejrowski</url>
        </developer>
      </developers>
        <scm>
          <url>https://github.com/Tapad/sbt-gcs</url>
          <connection>scm:git:git://github.com/Tapad/sbt-gcs.git</connection>
        </scm>
    }
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