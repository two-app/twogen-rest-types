val Http4sVersion = "0.21.14"
val TapirVersion = "0.17.19"
val CirceVersion = "0.13.0"

ThisBuild / scalaVersion := "{{cookiecutter.scala_version}}"

lazy val root = (project in file("."))
  .settings(
    organization := "com.two",
    name := "{{cookiecutter.name}}",
    version := "0.1.0",
    libraryDependencies ++= Seq(
      /* Http4s */
      "org.http4s" %% "http4s-dsl" % Http4sVersion,
      /* Tapir */
      "com.softwaremill.sttp.tapir" %% "tapir-core" % TapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-openapi-docs" % TapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-openapi-circe-yaml" % TapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-json-circe" % TapirVersion,
      /* Circe */
      "io.circe" %% "circe-generic" % CirceVersion,
      "io.circe" %% "circe-parser" % CirceVersion,
      /* Testing */
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
