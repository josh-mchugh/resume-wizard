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

  @cask.get("/wizard/experience")
  def getWizardExperience() =
    if dslContext.fetchCount(RESUME_EXPERIENCES) > 0 then
      val result = dslContext.fetchOne(RESUME_EXPERIENCES)
      val form = buildForm(Step.Experience, buildExperienceForm(result.getTitle(), result.getOrganization(), result.getDuration(), result.getLocation(), result.getDescription(), result.getSkills()))
      buildPage(buildSteps(Step.Experience), form)
    else
      val form = buildForm(Step.Experience, buildExperienceForm("", "", "", "", "", ""))
      buildPage(buildSteps(Step.Experience), form) 

  @cask.postForm("/wizard/experience")
  def postWizardExperience(title: String, organization: String, duration: String, location: String, description: String, skills: String) =
    if dslContext.fetchCount(RESUME_EXPERIENCES) > 0 then
      val resumeDetail = dslContext.selectFrom(RESUME_EXPERIENCES).fetchOne()
      dslContext.update(RESUME_EXPERIENCES)
        .set(RESUME_EXPERIENCES.TITLE, title)
        .set(RESUME_EXPERIENCES.ORGANIZATION, organization)
        .set(RESUME_EXPERIENCES.DURATION, duration)
        .set(RESUME_EXPERIENCES.LOCATION, location)
        .set(RESUME_EXPERIENCES.DESCRIPTION, description)
        .set(RESUME_EXPERIENCES.SKILLS, skills)
        .execute()
    else
      dslContext.insertInto(RESUME_EXPERIENCES, RESUME_EXPERIENCES.TITLE, RESUME_EXPERIENCES.ORGANIZATION, RESUME_EXPERIENCES.DURATION, RESUME_EXPERIENCES.LOCATION, RESUME_EXPERIENCES.DESCRIPTION, RESUME_EXPERIENCES.SKILLS)
        .values(title, organization, duration, location, description, skills)
        .execute()
    cask.Redirect("/wizard/skill")

  @cask.get("/wizard/skill")
  def getWizardSkill() =
    if dslContext.fetchCount(RESUME_SKILLS) > 0 then
      val result = dslContext.fetchOne(RESUME_SKILLS)
      val form = buildForm(Step.Skill, buildSkillForm(result.getName(), result.getRating()))
      buildPage(buildSteps(Step.Skill), form)
    else
      val form = buildForm(Step.Skill, buildSkillForm("", 0))
      buildPage(buildSteps(Step.Skill), form) 

  @cask.postForm("/wizard/skill")
  def postWizardSkill(name: String, rating: Short) =
    if dslContext.fetchCount(RESUME_SKILLS) > 0 then
      val resumeDetail = dslContext.selectFrom(RESUME_SKILLS).fetchOne()
      dslContext.update(RESUME_SKILLS)
        .set(RESUME_SKILLS.NAME, name)
        .set(RESUME_SKILLS.RATING, rating)
        .execute()
    else
      dslContext.insertInto(RESUME_SKILLS, RESUME_SKILLS.NAME, RESUME_SKILLS.RATING)
        .values(name, rating)
        .execute()
    cask.Redirect("/wizard/certification")

  @cask.get("/wizard/certification")
  def getWizardCertification() =
    if dslContext.fetchCount(RESUME_CERTIFICATIONS) > 0 then
      val result = dslContext.fetchOne(RESUME_CERTIFICATIONS)
      val form = buildForm(Step.Certification, buildCertificationForm(result.getTitle(), result.getOrganization(), result.getYear(), result.getLocation()))
      buildPage(buildSteps(Step.Certification), form)
    else
      val form = buildForm(Step.Certification, buildCertificationForm("", "", "", ""))
      buildPage(buildSteps(Step.Certification), form)

  @cask.postForm("/wizard/certification")
  def postWizardCertification(title: String, organization: String, year: String, location: String) =
    if dslContext.fetchCount(RESUME_CERTIFICATIONS) > 0 then
      val resumeDetail = dslContext.selectFrom(RESUME_CERTIFICATIONS).fetchOne()
      dslContext.update(RESUME_CERTIFICATIONS)
        .set(RESUME_CERTIFICATIONS.TITLE, title)
        .set(RESUME_CERTIFICATIONS.ORGANIZATION, organization)
        .set(RESUME_CERTIFICATIONS.YEAR, year)
        .set(RESUME_CERTIFICATIONS.LOCATION, location)
        .execute()
    else
      dslContext.insertInto(RESUME_CERTIFICATIONS, RESUME_CERTIFICATIONS.TITLE, RESUME_CERTIFICATIONS.ORGANIZATION, RESUME_CERTIFICATIONS.YEAR, RESUME_CERTIFICATIONS.LOCATION)
        .values(title, organization, year, location)
        .execute()
    cask.Redirect("/wizard/review")

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

  def buildExperienceForm(title: String, organization: String, duration: String, location: String, description: String, skills: String) =
    List(
      div()(
        label(cls := "form-label")("Title"),
        input(cls := "form-control", `type` := "text", name := "title", placeholder := "Title", value := title)
      ),
      div(cls := "mt-3")(
        label(cls := "form-label")("Organization"),
        input(cls := "form-control", `type` := "text", name := "organization", placeholder := "Organization", value := organization)
      ),
      div(cls := "mt-3")(
        label(cls := "form-label")("Duration"),
        input(cls := "form-control", `type` := "text", name := "duration",  placeholder := "Duration", value := duration)
      ),
      div(cls := "mt-3")(
        label(cls := "form-label")("Location"),
        input(cls := "form-control", `type` := "text", name := "location", placeholder := "Location", value := location)
      ),
      div(cls := "mt-3")(
        label(cls := "form-label")("Description"),
        textarea(cls := "form-control", rows := 3, name := "description", placeholder := "Description")(description)
      ),
      div(cls := "mt-3")(
        label(cls := "form-label")("Skills"),
        textarea(cls := "form-control", rows := 3, name := "skills",  placeholder := "Skills")(skills)
      )
    )

  def buildSkillForm(skillName: String, rating: Short) =
    List(
      div()(
        label(cls := "form-label")("Name"),
        input(cls := "form-control", `type` := "text", name := "name", placeholder := "Name", value := skillName)
      ),
      div(cls := "mt-3")(
        label(cls := "form-label")("Rating"),
        input(cls := "form-range", `type` := "range", name := "rating", min := 0, max := 5, step := 1, value := rating)
      )
    )

  def buildCertificationForm(title: String, organization: String, year: String, location: String) =
    List(
      div()(
        label(cls := "form-label")("Title"),
        input(cls := "form-control", `type` := "text", name := "title", placeholder := "Title", value := title)
      ),
      div(cls := "mt-3")(
        label(cls := "form-label")("Organization"),
        input(cls := "form-control", `type` := "text", name := "organization", placeholder := "Organization", value := organization)
      ),
      div(cls := "mt-3")(
        label(cls := "form-label")("Year"),
        input(cls := "form-control", `type` := "text", name := "year", placeholder := "Year", value := year)
      ),
      div(cls := "mt-3")(
        label(cls := "form-label")("Location"),
        input(cls := "form-control", `type` := "text", name := "location", placeholder := "Location", value := location)
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
