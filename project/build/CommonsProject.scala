import sbt._

class CommonsProject(info: ProjectInfo) extends DefaultProject(info){//} with ChecksumPlugin{	
	
  val scalatest = "org.scalatest" % "scalatest" %
	    "1.2" % "test"
	
  Credentials(Path.userHome / ".ivy2" / ".credentials", log)

  val publishTo = {
	if(version.toString.endsWith("-SNAPSHOT"))
		"Sonatype Nexus Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
	else "Sonatype Nexus Staging" at "https://oss.sonatype.org/service/local/staging/deploy/maven2"
  }

  override def managedStyle = ManagedStyle.Maven

  override def deliverProjectDependencies = Nil
	override def packageDocsJar = defaultJarPath("-javadoc.jar")
  override def packageSrcJar= defaultJarPath("-sources.jar")
  lazy val sourceArtifact = Artifact.sources(artifactID)
  lazy val docsArtifact = Artifact.javadoc(artifactID)

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