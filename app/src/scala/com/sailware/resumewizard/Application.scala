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
    case Skill extends Step("Skills")
    case Certification extends Step("Certifications")
    case Review extends Step("Review")

  @cask.get("/wizard/:stepString")
  def getWizard(stepString: String) =
    val step = Step.valueOf(stepString)
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
                          buildSteps(step),
                          buildContent(step),
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

  @cask.post("/wizard/:stepString")
  def postWizard(stepString: String) =
    val step = Step.valueOf(stepString)
    val redirectUrl =
      if hasNext(step) then s"/wizard/${Step.fromOrdinal(step.ordinal + 1)}"
      else s"/wziard/$step"
    cask.Redirect(redirectUrl)

  def buildSteps(current: Step) = 
    ul(cls := "wizardly__steps")(
      for step <- Step.values
      yield buildStep(step, step == current)
    )

  def buildStep(step: Step, current: Boolean) =
    val label = s"${step.ordinal + 1}. ${step.label}"
    val stepText =
      if current then
        span(cls := "wizardly-step__link wizardly-step__link--current")(label)
      else
        a(cls := "wizardly-step__link wizardly-step__link--active", href := s"/wizard/$step")(label)

    li(cls := "wizardly-step")(
      stepText
    )

  def buildContent(step: Step) =
    step match
      case Step.Name => buildForm(step, buildNameAndTitleForm())
      case Step.Contact => buildForm(step, buildContactsForm())
      case Step.Social => buildForm(step, buildSocialsForm())
      case Step.Experience => buildForm(step, buildExperienceForm())
      case Step.Skill => buildForm(step, buildSkillForm())
      case Step.Certification => buildForm(step, buildCertificationForm())
      case Step.Review => buildReview(step)

  def buildForm(step: Step, formInputs: Frag) =
    form(method := "post", action := s"/wizard/$step")(
      div(cls := "wizardly__content")(
        div(cls := "wizardly-form")(
          formInputs,
        ),
        buildActions(step)
      )
    )

  def buildNameAndTitleForm() =
    List(
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

  def buildContactsForm() =
    List(
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

  def buildSocialsForm() =
    List(
      div()(
        label(cls := "form-label")("Name"),
        input(cls := "form-control", `type` := "text", placeholder := "Name")
      ),
      div(cls := "mt-3")(
        label(cls := "form-label")("URL"),
        input(cls := "form-control", `type` := "text", placeholder := "URL")
      )
    )

  def buildExperienceForm() =
    List(
      div()(
        label(cls := "form-label")("Title"),
        input(cls := "form-control", `type` := "text", placeholder := "Title")
      ),
      div(cls := "mt-3")(
        label(cls := "form-label")("Organization"),
        input(cls := "form-control", `type` := "text", placeholder := "Organization")
      ),
      div(cls := "mt-3")(
        label(cls := "form-label")("Duration"),
        input(cls := "form-control", `type` := "text", placeholder := "Duration")
      ),
      div(cls := "mt-3")(
        label(cls := "form-label")("Location"),
        input(cls := "form-control", `type` := "text", placeholder := "Location")
      ),
      div(cls := "mt-3")(
        label(cls := "form-label")("Description"),
        textarea(cls := "form-control", rows := 3, placeholder := "Description")
      ),
      div(cls := "mt-3")(
        label(cls := "form-label")("Skills"),
        textarea(cls := "form-control", rows := 3, placeholder := "Skills")
      )
    )

  def buildSkillForm() =
    List(
      div()(
        label(cls := "form-label")("Name"),
        input(cls := "form-control", `type` := "text", placeholder := "Name")
      ),
      div(cls := "mt-3")(
        label(cls := "form-label")("Rating"),
        input(cls := "form-range", `type` := "range", min := 0, max := 5, step := 1)
      )
    )

  def buildCertificationForm() =
    List(
      div()(
        label(cls := "form-label")("Title"),
        input(cls := "form-control", `type` := "text", placeholder := "Title")
      ),
      div(cls := "mt-3")(
        label(cls := "form-label")("Organization"),
        input(cls := "form-control", `type` := "text", placeholder := "Organization")
      ),
      div(cls := "mt-3")(
        label(cls := "form-label")("Year"),
        input(cls := "form-control", `type` := "text", placeholder := "Year")
      ),
      div(cls := "mt-3")(
        label(cls := "form-label")("Location"),
        input(cls := "form-control", `type` := "text", placeholder := "Location")
      )
    )

  def buildActions(current: Step) =
    div(cls := "wizardly-actions")(
      if hasNext(current) then button(cls := "btn btn-primary", `type` := "submit")("Next") else ""
    )

  def hasNext(step: Step) =
    step.ordinal != Step.values.length - 1

  def buildReview(step: Step) =
    div()("Review Page")

  initialize()

object Application extends cask.Main:
  val allRoutes = Seq(
    StaticRoutes(),
    RootRoutes()
  )
