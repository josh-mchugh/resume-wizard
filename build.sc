import mill._, scalalib._

object app extends ScalaModule {
  def scalaVersion = "3.3.3"

  def ivyDeps = Agg(
    ivy"com.lihaoyi::cask:0.9.2",
    ivy"com.lihaoyi::scalatags:0.13.1",
    ivy"org.jooq:jooq:3.19.10",
    ivy"org.postgresql:postgresql:42.7.3",
    ivy"org.flywaydb:flyway-core:10.15.2",
    ivy"org.flywaydb:flyway-database-postgresql:10.15.2"
  )

  object test extends ScalaTests {
    def testFramework = "utest.runner.Framework"
    def ivyDeps = Agg(
      ivy"com.lihaoyi::utest::0.8.3",
      ivy"com.lihaoyi::requests::0.8.3",
      ivy"io.undertow:undertow-core:2.3.10.Final",
    )
  }
}