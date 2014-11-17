
organization := "scala"

name := "rabbitlistener"

version := "1.0"

scalaVersion := "2.10.0"

libraryDependencies += "joda-time" % "joda-time" % "1.6.2"

libraryDependencies += "com.rabbitmq" % "amqp-client" % "2.4.1"

libraryDependencies += "org.apache.spark" %% "spark-core" % "1.1.0" exclude("org.apache.hadoop","hadoop-client")

libraryDependencies += "org.apache.hadoop" % "hadoop-client" % "2.4.0"

libraryDependencies += "org.apache.spark" % "spark-streaming_2.10" % "1.1.0" exclude("org.apache.hadoop","hadoop-client")

libraryDependencies += "com.fasterxml.jackson.core" % "jackson-core" % "2.4.3"

libraryDependencies += "org.apache.spark" % "spark-mllib_2.10" % "1.1.0"

