name := "recursivity-commons"

version := "0.6"

organization := "com.recursivity"

scalaVersion := "2.10.4"

//artifact-name := "bingo"

resolvers ++= Seq(
  "Sonatype OSS Snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/",
  "Sonatype OSS Releases" at "https://oss.sonatype.org/content/repositories/releases",
  "Novus Salat Snapshots" at "http://repo.novus.com/snapshots",
  "Scala-Tools repo" at "http://scala-tools.org/repo-releases/",
  "Spring Repo" at "http://maven.springframework.org/milestone",
  "Buzz Media" at "http://maven.thebuzzmedia.com",
  "Codahale" at "http://repo.codahale.com"
)

resolvers += "Local Maven Repository" at "file://"+Path.userHome.absolutePath+"/.m2/repository"

libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.4" % "test"

libraryDependencies += "org.scala-lang" % "scalap" % "2.10.4"

libraryDependencies += "org.scala-lang" % "scala-reflect" % "2.10.4"