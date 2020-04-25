import sbt.Project.projectToRef

name := "Main Play Project"
version := "1.0-SNAPSHOT"

updateOptions := updateOptions.value.withCachedResolution(true)

lazy val scalaV = "2.11.11"
lazy val sharedJvm = shared.jvm
lazy val sharedJs = shared.js

lazy val clients = Seq(client)

lazy val server = (project in file("server")).settings(
  scalaVersion := scalaV,
  scalaJSProjects := clients,
  pipelineStages := Seq(scalaJSProd, gzip),
  //resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases",
  libraryDependencies ++= Seq(
    filters,
    "com.vmunier" %% "play-scalajs-scripts" % "0.4.0"
  )
).enablePlugins(PlayScala)
  .aggregate(clients.map(projectToRef): _*)
  .dependsOn(sharedJvm)

lazy val shared = (crossProject.crossType(CrossType.Pure) in file("shared")).
  settings(
    scalaVersion := scalaV,
    libraryDependencies ++= Seq(
      "com.lihaoyi" %%% "scalatags" % "0.5.4"
    )
  ).jsConfigure(_ enablePlugins ScalaJSPlay)

lazy val client = (project in file("client")).settings(
  scalaVersion := scalaV,
 // persistLauncher := false,
//  mainClass in Compile := Some("client.Main"),
 // persistLauncher in Test := false,
  libraryDependencies ++= Seq(
    "org.scala-js" %%% "scalajs-dom" % "0.8.0",
    "com.thoughtworks.binding" %%% "binding"     % "10.0.2",
    "com.thoughtworks.binding" %%% "dom"         % "10.0.2"
  ),
  addCompilerPlugin(
    "org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full
  ),
  scalaJSUseMainModuleInitializer := true
).enablePlugins(ScalaJSPlugin, ScalaJSPlay).
  dependsOn(sharedJs)


onLoad in Global := (Command.process("project server", _: State)) compose (onLoad in Global).value