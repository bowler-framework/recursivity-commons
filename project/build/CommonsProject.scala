import sbt._

class CommonsProject(info: ProjectInfo) extends DefaultProject(info){//} with ChecksumPlugin{
	
  val scalatest = {
    if(buildScalaVersion.contains("2.9"))
      "org.scalatest" % "scalatest_2.9.0" % "1.4.1" % "test"
    else
      "org.scalatest" % "scalatest" % "1.3" % "test"
  }

  val asm = "org.scala-lang" % "scalap" % buildScalaVersion
	
  Credentials(Path.userHome / ".ivy2" / ".credentials", log)

  val publishTo = {
		"libs-releases-local" at "http://xeralux.artifactoryonline.com/xeralux/simple/libs-releases-local/"
  }

  override def managedStyle = ManagedStyle.Maven

  override def deliverProjectDependencies = Nil
  override def packageDocsJar = defaultJarPath("-javadoc.jar")
  override def packageSrcJar= defaultJarPath("-sources.jar")
  lazy val sourceArtifact = Artifact.sources(artifactID)
  lazy val docsArtifact = Artifact.javadoc(artifactID)
  val scalaTestRepo = "Scala Test Repo" at "http://scala-tools.org/repo-snapshots"

  override def packageToPublishActions = super.packageToPublishActions ++ Seq(packageDocs, packageSrc)
  override def pomExtra = {

    // If these aren't lazy, then the build crashes looking for
    // ${moduleName}/project/build.properties.


	(
	    <name>{name}</name>
	    <description>Recursivity Commons Project POM</description>
	    <url>http://github.com/wfaler/recursivity-commons</url>
	    <inceptionYear>2010</inceptionYear>
	    <organization>
	      <name>Recursivity Commons Project</name>
	      <url>http://github.com/wfaler/recursivity-commons</url>
	    </organization>
	    <licenses>
	      <license>
	        <name>BSD</name>
	        <url>http://github.com/wfaler/recursivity-commons/LICENSE</url>
	        <distribution>repo</distribution>
	      </license>
	    </licenses>
	    <scm>
	      <connection>scm:git:git://github.com/wfaler/recursivity-commons.git</connection>
	      <url>http://github.com/wfaler/recursivity-commons</url>
	    </scm>
	    <developers>
	      <developer>
	        <id>wfaler</id>
	        <name>Wille Faler</name>
	        <url>http://blog.recursivity.com</url>
	      </developer>
	    </developers>)
  }
	
}