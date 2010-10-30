import sbt._

class CommonsProject(info: ProjectInfo) extends DefaultProject(info){	
	
	val scalatest = "org.scalatest" % "scalatest" %
	    "1.2" % "test"
	
}
