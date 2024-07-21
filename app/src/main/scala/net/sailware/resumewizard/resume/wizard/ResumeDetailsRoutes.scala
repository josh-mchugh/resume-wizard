package net.sailware.resumewizard.resume.wizard

import net.sailware.resumewizard.resume.ResumeDetailsRepository
import net.sailware.resumewizard.resume.ResumePageView
import net.sailware.resumewizard.resume.Step
import scalatags.Text.all.*

case class ResumeDetailsRoutes(repository: ResumeDetailsRepository)(implicit cc: castor.Context, log: cask.Logger) extends cask.Routes:

  @cask.get("/wizard/detail")
  def getWizardName() =
    if repository.fetchCount() > 0 then
      val result = repository.fetchOne()
      val form = ResumePageView.buildForm(
        Step.Detail,
        buildNameAndTitleForm(result.getName(), result.getTitle(), result.getSummary(), result.getPhone(), result.getEmail(), result.getLocation())
      )
      ResumePageView.buildPage(ResumePageView.buildSteps(Step.Detail), form)
    else
      val form = ResumePageView.buildForm(Step.Detail, buildNameAndTitleForm("", "", "", "", "", ""))
      ResumePageView.buildPage(ResumePageView.buildSteps(Step.Detail), form) 

  @cask.postForm("/wizard/detail")
  def postWizardName(name: String, title: String, summary: String, phone: String, email: String, location: String) =
    if repository.fetchCount() > 0 then
      val result = repository.fetchOne()
      repository.update(result.getId(), name, title, summary, phone, email, location)
    else
      repository.insert(name, title, summary, phone, email, location)
    cask.Redirect("/wizard/social")

  def buildNameAndTitleForm(resumeName: String, title: String, summary: String, phone: String, email: String, location: String) =
    List(
      div()(
        label(cls := "form-label")("Name"),
        input(cls := "form-control", `type` := "text", name := "name", placeholder := "Name", value := resumeName)
      ),
      div(cls := "mt-3")(
        label(cls := "form-label")("Title"),
        input(cls := "form-control", `type` := "text", name := "title", placeholder := "Title", value := title)
      ),
      div(cls := "mt-3")(
        label(cls := "form-label")("Summary"),
        textarea(cls := "form-control", rows := 3, name := "summary",  placeholder := "Summary of your current or previous role")(summary)
      ),
      div()(
        label(cls := "form-label")("Phone Number"),
        input(cls := "form-control", `type` := "text", name := "phone", placeholder := "Phone Number", value := phone)
      ),
      div(cls := "mt-3")(
        label(cls := "form-label")("Email Address"),
        input(cls := "form-control", `type` := "text", name := "email", placeholder := "Email Address", value := email)
      ),
      div(cls := "mt-3")(
        label(cls := "form-label")("Location"),
        input(cls := "form-control", `type` := "text", name := "location", placeholder := "Location", value := location)
      )
    )

  initialize()



