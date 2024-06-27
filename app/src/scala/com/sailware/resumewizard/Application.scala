package com.sailware.resumewizard

import scalatags.Text.all.*

case class Routes()(implicit cc: castor.Context, log: cask.Logger) extends cask.Routes:

  @cask.get("/")
  def hello() =
    doctype("html")(
      html(
        body(
          h1("Hello World!")
        )
      )
    )

  initialize()

object Application extends cask.Main:
  val allRoutes = Seq(Routes())
