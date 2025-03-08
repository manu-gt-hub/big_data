name := "twitter-stream-final"

version := "0.1"

scalaVersion := "2.11.12"

val sparkVersion = "2.3.0"

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x => MergeStrategy.first
}
