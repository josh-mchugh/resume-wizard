package net.sailware.resumewizard

import scalatags.Text.all.*
import scalatags.Text.tags2.nav
import scalatags.Text.tags2.section
import scalatags.Text.tags2.title
import net.sailware.resumewizard.database.DatabaseResource
import net.sailware.resumewizard.jooq.Tables.*
import net.sailware.resumewizard.jooq.tables.records.*
import net.sailware.resumewizard.resume.Step

import scala.jdk.OptionConverters.RichOptional

case class RootRoutes(databaseResource: DatabaseResource)(implicit cc: castor.Context, log: cask.Logger) extends cask.Routes:
  val dslContext = databaseResource.ctx

  @cask.get("/wizard/review")
  def getWizardReview() =
    val detailsOption = dslContext.fetchOptional(RESUME_DETAILS).toScala
    val socialsOption = dslContext.fetchOptional(RESUME_SOCIALS).toScala
    val experiencesOption = dslContext.fetchOptional(RESUME_EXPERIENCES).toScala
    val skillsOption = dslContext.fetchOptional(RESUME_SKILLS).toScala
    val certificationsOption = dslContext.fetchOptional(RESUME_CERTIFICATIONS).toScala
    buildPage(buildSteps(Step.Review), buildReview(detailsOption, socialsOption, experiencesOption, skillsOption, certificationsOption))

  def buildPage(steps: Frag, content: Frag) =
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
                          steps,
                          content,
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
      for step <- Step.values
      yield buildStep(step, step == current)
    )

  def buildStep(step: Step, current: Boolean) =
    val label = s"${step.ordinal + 1}. ${step.label}"
    val stepText =
      if current then
        span(cls := "wizardly-step__link wizardly-step__link--current")(label)
      else
        a(cls := "wizardly-step__link wizardly-step__link--active", href := s"/wizard/${step.toString().toLowerCase()}")(label)

    li(cls := "wizardly-step")(
      stepText
    )

  def buildForm(step: Step, formInputs: Frag) =
    form(method := "post", action := s"/wizard/${step.toString().toLowerCase()}")(
      div(cls := "wizardly__content")(
        div(cls := "wizardly-form")(
          formInputs,
        ),
        buildActions(step)
      )
    )

  def buildActions(current: Step) =
    div(cls := "wizardly-actions")(
      if hasNext(current) then button(cls := "btn btn-primary", `type` := "submit")("Next") else ""
    )

  def hasNext(step: Step) =
    step.ordinal != Step.values.length - 1

  def buildReview(detailsOption: Option[ResumeDetailsRecord], socialsOption: Option[ResumeSocialsRecord], experiencesOption: Option[ResumeExperiencesRecord], skillsOption: Option[ResumeSkillsRecord], certificationsOption: Option[ResumeCertificationsRecord]) =
    val details = List(
        p(strong("Name: "), detailsOption.get.getName()),
        p(strong("Title: "), detailsOption.get.getTitle()),
        p(strong("Summary: "), detailsOption.get.getSummary()),
        p(strong("Phone: "), detailsOption.get.getPhone()),
        p(strong("Email: "), detailsOption.get.getEmail()),
        p(strong("Location: "), detailsOption.get.getLocation()),
    )

    val socials = List(
      p(strong("Social Name: "), socialsOption.get.getName()),
      p(strong("Social URL: "), socialsOption.get.getUrl()),
    )

    val experiences = List(
      p(strong("Experience Title: "), experiencesOption.get.getTitle()),
      p(strong("Experience Organization: "), experiencesOption.get.getOrganization()),
      p(strong("Experience Duration: "), experiencesOption.get.getDuration()),
      p(strong("Experience Location: "), experiencesOption.get.getLocation()),
      p(strong("Experience Description: "), experiencesOption.get.getDescription()),
      p(strong("Experience Skills: "), experiencesOption.get.getSkills()),
    )

    val skills = List(
      p(strong("Skill Name: "), skillsOption.get.getName()),
      p(strong("Skill Rating: "), skillsOption.get.getRating().toString()),
    )

    val certifications = List(
      p(strong("Certification Title: ", certificationsOption.get.getTitle())),
      p(strong("Certification Organization: "), certificationsOption.get.getOrganization()),
      p(strong("Certification Year: "), certificationsOption.get.getYear()),
      p(strong("Certification Location: "), certificationsOption.get.getLocation()),
    )

    div()(
      div(id := "main")(
        if detailsOption.isDefined then details else frag(),
        if socialsOption.isDefined then socials else frag(),
        if experiencesOption.isDefined then experiences else frag(),
        if skillsOption.isDefined then skills else frag(),
        if certificationsOption.isDefined then certifications else frag()
      )
    )

  initialize()
