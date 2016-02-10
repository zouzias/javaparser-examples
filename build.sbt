name := "javaparser-examples"

version := "0.1.0-SNAPSHOT"

organization := "org.zouzias.javaparser"

scalaVersion := "2.11.7"

crossScalaVersions := Seq("2.11.7")

libraryDependencies ++= Seq(
	"com.google.code.javaparser"	% "javaparser"        % "1.0.11",
	"commons-io"									% "commons-io"        % "2.4",
	"org.scalatest"     					%% "scalatest" 		    % "2.2.5" % "test"
)

lazy val compileScalastyle = taskKey[Unit]("compileScalastyle")

compileScalastyle := org.scalastyle.sbt.ScalastylePlugin.scalastyle.in(Compile).toTask("").value

(compile in Compile) <<= (compile in Compile) dependsOn compileScalastyle




