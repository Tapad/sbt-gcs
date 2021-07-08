import Dependencies._
import Publishing._

sbtPlugin := true

crossSbtVersions := Seq("0.13.17", "1.1.4")

name := "sbt-gcs"

organization := "com.tapad.sbt"

licenses += ("BSD New", url("https://opensource.org/licenses/BSD-3-Clause"))

scalacOptions ++= Seq("-deprecation", "-language:_")

libraryDependencies ++= Seq(
  "org.scalatest"      %% "scalatest"            % ScalaTestVersion  % Test,
  "org.slf4j"           % "slf4j-api"            % SLF4JVersion,
  "org.slf4j"           % "log4j-over-slf4j"     % SLF4JVersion,
  "com.google.cloud"    % "google-cloud-storage" % GcsClientVersion
)

enablePlugins(SbtPlugin)

scriptedLaunchOpts ++= Seq("-Xmx1024M", "-Dplugin.version=" + version.value)

scriptedBufferLog := false

PublishSettings

ReleaseSettings
