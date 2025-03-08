name := "proyectoInserciones"

version := "0.1"

scalaVersion := "2.11.12"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % "test"

libraryDependencies += "org.apache.spark" %% "spark-core" % "2.3.0"

libraryDependencies += "org.apache.spark" %% "spark-sql" % "2.3.0"

libraryDependencies += "com.typesafe" % "config" % "1.3.3"

libraryDependencies += "org.elasticsearch" % "elasticsearch-hadoop" % "6.2.3"

libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.6" % Test

libraryDependencies += "org.scala-lang" % "scala-library" % scalaVersion.value

libraryDependencies += "org.mongodb.spark" %% "mongo-spark-connector" % "2.3.1"

