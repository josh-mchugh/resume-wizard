package net.sailware.resumewizard.resume.wizard

import net.sailware.resumewizard.resume.ResumeExperiencesRepository
import net.sailware.resumewizard.resume.ResumePageView
import net.sailware.resumewizard.resume.Step
import scalatags.Text.all.*

case class ResumeExperienceRoutes(repository: ResumeExperiencesRepository)(implicit cc: castor.Context, log: cask.Logger) extends cask.Routes:

  @cask.get("/wizard/experience")
  def getWizardExperience() =
    if repository.fetchCount() > 0 then
      val result = repository.fetchOne()
      val form = ResumePageView.buildForm(
        Step.Experience,
        buildExperienceForm(
          result.getTitle(),
          result.getOrganization(),
          result.getDuration(),
          result.getLocation(),
          result.getDescription(),
          result.getSkills())
      )
      ResumePageView.buildPage(ResumePageView.buildSteps(Step.Experience), form)
    else
      val form = ResumePageView.buildForm(Step.Experience, buildExperienceForm("", "", "", "", "", ""))
      ResumePageView.buildPage(ResumePageView.buildSteps(Step.Experience), form) 

  @cask.postForm("/wizard/experience")
  def postWizardExperience(title: String, organization: String, duration: String, location: String, description: String, skills: String) =
    if repository.fetchCount() > 0 then
      val resumeDetail = repository.fetchOne()
      repository.update(title, organization, duration, location, description, skills)
    else
      repository.insert(title, organization, duration, location, description, skills)
    cask.Redirect("/wizard/skill")

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

  initialize()
