name := """myAppBank"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

routesGenerator := InjectedRoutesGenerator

libraryDependencies ++= Seq(
  "org.reactivemongo" %% "play2-reactivemongo" % "0.11.11",
  "com.firebase" % "firebase-client" % "1.0.0"
)


resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"
resolvers += "central" at "http://repo1.maven.org/maven2"

scalacOptions in ThisBuild ++= Seq("-feature", "-language:postfixOps")

fork in run := true

fork in run := true

fork in run := true