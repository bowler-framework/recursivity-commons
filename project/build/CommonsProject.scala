import sbt._

class CommonsProject(info: ProjectInfo) extends DefaultProject(info){	
	
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
	
}
