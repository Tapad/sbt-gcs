addSbtPlugin("org.foundweekends" % "sbt-bintray" % "0.5.4")

addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.9.0")

addSbtPlugin("com.github.gseitz" % "sbt-release" % "1.0.8")

libraryDependencies += "org.scala-sbt" % "scripted-plugin" % sbtVersion.value