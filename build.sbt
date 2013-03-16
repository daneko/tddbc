import scalariform.formatter.preferences._
import de.johoop.jacoco4sbt._
import JacocoPlugin._

name := "Tddbc"

version := "0.1-SNAPSHOT"

scalaVersion := "2.10.0"

libraryDependencies ++= Seq(
  "org.specs2" %% "specs2" % "1.14" % "test",
  "org.scalacheck" %% "scalacheck" % "1.10.0" % "test",
  "org.scalamock" %% "scalamock-specs2-support" % "3.0.1" % "test",
  "junit" % "junit" % "4.7" % "test",
  "org.pegdown" % "pegdown" % "1.0.2"
)

// Read here for optional dependencies:
// http://etorreborre.github.com/specs2/guide/org.specs2.guide.Runners.html#Dependencies

testOptions in Test += Tests.Argument("junitxml", "html", "console")

resolvers ++= Seq(
  "snapshots" at "http://oss.sonatype.org/content/repositories/snapshots",
  "releases"  at "http://oss.sonatype.org/content/repositories/releases"
)

scalariformSettings

ScalariformKeys.preferences := FormattingPreferences()
  .setPreference(AlignSingleLineCaseStatements, true)

seq(jacoco.settings : _*)

jacoco.reportFormats in jacoco.Config := Seq(XMLReport("utf-8"), HTMLReport("utf-8"))

jacoco.outputDirectory in jacoco.Config := new File("target/jacoco/")

