val TapirVersion = "0.17.19"
val CirceVersion = "0.13.0"

ThisBuild / scalaVersion := { { cookiecutter.scala_version } }

lazy val root = (project in file("."))
  .settings(
    organization := "com.two",
    name := "{{cookiecutter.name}}",
    version := "0.1.0",
    libraryDependencies ++= Seq(
      "com.softwaremill.sttp.tapir" %% "tapir-core" % TapirVersion,
      "io.circe" %% "circe-generic" % CirceVersion,
      "io.circe" %% "circe-parser" % CirceVersion,
      /** Logging */
      "ch.qos.logback" % "logback-classic" % "1.2.3",
      "org.typelevel" % "log4cats-slf4j_2.13" % "1.2.0-RC2",
      "net.logstash.logback" % "logstash-logback-encoder" % "6.6",
      /** Libraries */
      "com.pauldijou" %% "jwt-core" % "4.3.0",
      /** Testing */
      "org.scalameta" %% "munit" % "0.7.22" % Test
    ),
    addCompilerPlugin("org.typelevel" % "kind-projector_2.13.4" % "0.11.2"),
    addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1"),
    scalacOptions += "-Ymacro-annotations"
  )

/** Scalafix Configuration */
ThisBuild / scalafixDependencies += "com.github.liancheng" %% "organize-imports" % "0.5.0"
inThisBuild(
  List(
    scalaVersion := "{{cookiecutter.scala_version}}", // TODO can this be removed because of the THisBuild?
    semanticdbEnabled := true,
    semanticdbVersion := scalafixSemanticdb.revision
  )
)

/** GitHub Maven Packages Deployment + Resolver Configuration */
ThisBuild / githubOwner := "two-app"
ThisBuild / githubRepository := "t_user"
ThisBuild / resolvers += Resolver.githubPackages("OWNER")
ThisBuild / githubTokenSource := TokenSource.Environment("GITHUB_TOKEN")
