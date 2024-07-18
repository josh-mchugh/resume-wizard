package net.sailware.resumewizard.resume.wizard

import net.sailware.resumewizard.resume.ResumeContactRepository
import net.sailware.resumewizard.resume.ResumePageView
import net.sailware.resumewizard.resume.Step
import scalatags.Text.all.*

case class ResumeContactRoutes(repository: ResumeContactRepository)(implicit cc: castor.Context, log: cask.Logger) extends cask.Routes:

  @cask.get("/wizard/contact")
  def getWizardContact() =
    if repository.fetchCount() > 0 then
      val result = repository.fetchOne()
      val form = ResumePageView.buildForm(
        Step.Contact,
        buildContactsForm(result.getPhone(), result.getEmail(), result.getLocation())
      )
      ResumePageView.buildPage(ResumePageView.buildSteps(Step.Contact), form)
    else
      val form = ResumePageView.buildForm(Step.Contact, buildContactsForm("", "", ""))
      ResumePageView.buildPage(ResumePageView.buildSteps(Step.Contact), form) 

  @cask.postForm("/wizard/contact")
  def postWizardContact(phone: String, email: String, location: String) =
    if repository.fetchCount() > 0 then
      val resumeDetail = repository.fetchOne()
      repository.update(phone, email, location)
    else
      repository.insert(phone, email, location)
    cask.Redirect("/wizard/social")

  def buildContactsForm(phone: String, email: String, location: String) =
    List(
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
