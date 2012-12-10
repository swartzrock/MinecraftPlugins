name := "JCDC Plugin Factory"

version := "0.1"

organization := "jcdc.pluginfactory"

scalaVersion := "2.10.0-M7"

mainClass := Some("jcdc.pluginfactory.MineLangRepl")

resolvers += Resolver.sonatypeRepo("snapshots")

resolvers ++= Seq(
  "Sonatype Snapshots" at "http://oss.sonatype.org/content/repositories/snapshots",
  "Sonatype Releases"  at "http://oss.sonatype.org/content/repositories/releases",
  "Bukkit"             at "http://repo.bukkit.org/content/repositories/releases"
)

resolvers += Resolver.url(
  "sbt-plugin-releases",
  new URL("http://scalasbt.artifactoryonline.com/scalasbt/sbt-plugin-releases/")
)(Resolver.ivyStylePatterns)

addSbtPlugin("com.github.retronym" % "sbt-onejar" % "0.8")

libraryDependencies ++= Seq(
  "javax.servlet" % "servlet-api" % "2.5" % "provided->default",
  "org.bukkit" % "craftbukkit" % "1.4.5-R0.2",
  "ch.spacebase" % "NPCCreatures" % "1.4" from "http://dev.bukkit.org/media/files/584/232/NPCCreatures.jar",
  // TODO: put this back when 2.10 is finally out, i think
  //"org.scalacheck" %% "scalacheck" % "1.10.0" % "test",
  "org.scalacheck" % "scalacheck_2.10.0-M7" % "1.10.0" % "test",
  "org.scala-lang" % "jline" % "2.10.0-M7",
  "org.scalaz" % "scalaz-core_2.10.0-M7" % "7.0.0-M3",
  "org.clojure" % "clojure" % "1.4.0"
)