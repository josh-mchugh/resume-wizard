package com.sailware.resumewizard

import scalatags.Text.all.*
import scalatags.Text.tags2.nav
import scalatags.Text.tags2.title
import scalatags.Text.tags2.section

case class StaticRoutes()(implicit cc: castor.Context, log: cask.Logger) extends cask.Routes:

  @cask.staticFiles("/static/")
  def staticFilesRoutes() = "app/src/resources"

  initialize()

case class RootRoutes()(implicit cc: castor.Context, log: cask.Logger) extends cask.Routes:

  @cask.get("/")
  def hello() =
    doctype("html")(
      html(
        title("Resume Wizard"),
        head(link(rel := "stylesheet", href := "/static/style.min.css")),
        body(
          div(cls := "main-wrapper")(
            // top navigation
            nav(cls := "horizontal-menu")(
              div(cls := "navbar top-navbar")(
                div(cls := "container")(
                  div(cls := "navbar-content")(
                   a(cls :="navbar-brand", href := "#")("Resume", span("Wizard"))
                  )
                )
              )
            ),
            // Page Content
            div(cls := "page-wrapper")(
              div(cls := "page-content")(
                // Wizard
                div(cls := "row")(
                  div(cls := "col-md-12 stretch-card")(
                    div(cls := "card")(
                      div(cls := "card-body")(
                        h4(cls := "card-title")(),
                        div(id := "wizardVertical", cls := "wizard clearfix vertical", style := "display: block; width: 100%; overflow: hidden;", role := "application")(
                          div(cls := "steps clearfix", style := "display: inline; float: left; width: 30%;")(
                            ul(role := "tablist")(
                              li(cls := "first current", role := "tab", aria.disabled := "false", aria.selected := "true")(
                                a(
                                  span(cls := "number")("1."),
                                  "First Step"
                                )
                              ),
                              li(cls := "done", role := "tab", aria.disabled := "false", aria.selected := "true")(
                                a(
                                  span(cls := "number")("2."),
                                  "Second Step"
                                )
                              ),
                              li(cls := "disabled", role := "tab", aria.disabled := "false", aria.selected := "true")(
                                a(
                                  span(cls := "number")("3."),
                                  "Third Step"
                                )
                              ),
                              li(cls := "disabled", role := "tab", aria.disabled := "false", aria.selected := "true")(
                                a(
                                  span(cls := "number")("4."),
                                  "Four Step"
                                )
                              )
                            ),
                          ),
                          div(cls := "content clearfix", style := "float: left; position: relative; border-radius: 5px;")(
                            section(cls := "body current", role := "tabpanel", aria.hidden := "false", style := "left: 0px; float: left; position: absolute; width: 95%; height: 95%; padding: 2.5%;")(
                              h4("First Step"),
                              p("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer ut nulla nunc. Maecenas arcu sem, hendrerit a tempor quis, sagittis accumsan tellus. In hac habitasse platea dictumst. Donec a semper dui. Nunc eget quam libero. Nam at felis metus. Nam tellus dolor, tristique ac tempus nec, iaculis quis nisi.")
                            )
                          ),
                          div(cls := "actions clearfix", style := "float: right; position: relative; text-align: right;")(
                            ul(role := "menu", aria.disabled := "true", style := "display: block; list-style: none; padding: 0; margin: 0; text-align: right; display: inline-block;")(
                              li(cls := "disabled", aria.disabled := "true", style := "float: left; margin: 0 0 0 1em;")(
                                a(role := "menuitem")("Previous")
                              ),
                              li(aria.hidden := "false", aria.disabled := "false", style := "float: left; margin: 0 0 0 1em")(
                                a(role := "menuitem")("Next")
                              ),
                            )
                          )
                        )
                      )
                    )
                  )
                )
              ),
              // Footer
              footer(cls := "footer border-top")(
                div(cls := "container d-flex flex-column flex-md-row align-items-center justify-content-between py-3 small")(
                  p(cls := "text-muted mb-1 mb-md-0")("Copyright 2024"),
                  p(cls := "text-muted")("Maded For Fun")
                )
              )
            )
          )
        )
      )
    )

  initialize()

object Application extends cask.Main:
  val allRoutes = Seq(
    StaticRoutes(),
    RootRoutes()
  )
