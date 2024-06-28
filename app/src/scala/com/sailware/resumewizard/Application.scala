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
        head(
          link(rel := "stylesheet", href := "/static/style.min.css"),
          link(rel := "stylesheet", href := "/static/styles.css")
        ),
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
                // Wizardly
                div(cls := "row")(
                  div(cls := "col-md-12 stretch-card")(
                    div(cls := "card")(
                      div(cls := "card-body")(
                        h4(cls := "card-title")("Resume Wizard"),
                        div(cls := "wizardly")(
                          ul(cls := "wizardly__steps")(
                            li(cls := "wizardly-step")(
                              span(cls := "wizardly-step__link wizardly-step__link--current")("1. Name & Title")
                            ),
                            li(cls := "wizardly-step")(
                              a(cls := "wizardly-step__link wizardly-step__link--active")("2. Contacts")
                            ),
                            li(cls := "wizardly-step")(
                              span(cls := "wizardly-step__link")("3. Socials")
                            ),
                            li(cls := "wizardly-step")(
                              span(cls := "wizardly-step__link")("4. Experiences")
                            ),
                            li(cls := "wizardly-step")(
                              span(cls := "wizardly-step__link")("5. Certifications")
                            )
                          ),
                          div(cls := "wizardly__content")(
                            form()(
                              div(cls := "wizardly-form")(
                                div()(
                                  label(cls := "form-label")("Name"),
                                  input(cls := "form-control", `type` := "text", placeholder := "Name")
                                ),
                                div(cls := "mt-3")(
                                  label(cls := "form-label")("Title"),
                                  input(cls := "form-control", `type` := "text", placeholder := "Title")
                                ),
                                div(cls := "mt-3")(
                                  label(cls := "form-label")("Summary"),
                                  textarea(cls := "form-control", rows := 3, placeholder := "Summary of your current or previous role")
                                )
                              )
                            ),
                            div(cls := "wizardly-actions")(
                              button(cls := "btn btn-secondary btn-disabled")("Previous"),
                              button(cls := "btn btn-primary")("Next")
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
