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

  enum Step(val label: String):
    case Name extends Step("Name & Title")
    case Contact extends Step("Contacts")
    case Social extends Step("Socials")
    case Experience extends Step("Experiences")
    case Certification extends Step("Certifications")

  @cask.get("/")
  def hello() =
    val current = Step.Contact
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
                          buildSteps(current),
                          div(cls := "wizardly__content")(
                            buildForm(current),
                            buildActions(),
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

  def buildSteps(current: Step) = 
    ul(cls := "wizardly__steps")(
      for step <- Step.values.toList
      yield buildStep(s"${step.ordinal + 1}. ${step.label}", step == current)
    )

  def buildStep(label: String, current: Boolean) =
    val stepText =
      if current then
        span(cls := "wizardly-step__link wizardly-step__link--current")(label)
      else
        a(cls := "wizardly-step__link wizardly-step__link--active", href := s"#$label")(label)

    li(cls := "wizardly-step")(
      stepText
    )

  def buildForm(current: Step) =
    current match
      case Step.Name => buildNameAndTitleForm()
      case Step.Contact => buildContactsForm()
      case _ => buildErrorForm()

  def buildNameAndTitleForm() =
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
    )

  def buildContactsForm() =
    form()(
      div(cls := "wizardly-form")(
        div()(
          label(cls := "form-label")("Phone Number"),
          input(cls := "form-control", `type` := "text", placeholder := "Phone Number")
        ),
        div(cls := "mt-3")(
          label(cls := "form-label")("Email Address"),
          input(cls := "form-control", `type` := "text", placeholder := "Email Address")
        ),
        div(cls := "mt-3")(
          label(cls := "form-label")("Location"),
          input(cls := "form-control", `type` := "text", placeholder := "Location")
        )
      )
    )

  def buildErrorForm() =
    p("Unknown state, try again")

  def buildActions() =
    div(cls := "wizardly-actions")(
      button(cls := "btn btn-secondary btn-disabled")("Previous"),
      button(cls := "btn btn-primary")("Next")
    )

  initialize()

object Application extends cask.Main:
  val allRoutes = Seq(
    StaticRoutes(),
    RootRoutes()
  )
