enablePlugins(WorkbenchPlugin)

lazy val root = project
  .in(file("."))
  .enablePlugins(ScalaJSPlugin)
  .settings(
    inThisBuild(List(
      organization := "pl.tues",
      version      := "0.1-SNAPSHOT",
      scalaVersion := "2.11.11"
    )),
    name := "Tic-tac-toe",
    libraryDependencies ++= Seq(
      "org.scala-js"             %%% "scalajs-dom" % "1.0.0",
      "com.thoughtworks.binding" %%% "binding"     % "10.0.2",
      "com.thoughtworks.binding" %%% "dom"         % "10.0.2"
    ),
    addCompilerPlugin(
      "org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full
    ),
    scalaJSUseMainModuleInitializer := true
  )


