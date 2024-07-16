package net.sailware.resumewizard

case class StaticRoutes()(implicit cc: castor.Context, log: cask.Logger) extends cask.Routes:

  @cask.staticFiles("/static/")
  def staticFilesRoutes() = "app/src/main/resources/public"

  initialize()
