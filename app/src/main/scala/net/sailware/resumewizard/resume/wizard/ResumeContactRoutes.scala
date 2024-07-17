package net.sailware.resumewizard.resume.wizard

import net.sailware.resumewizard.database.DatabaseResource
import net.sailware.resumewizard.jooq.Tables.RESUME_DETAILS
import net.sailware.resumewizard.resume.ResumePageView
import net.sailware.resumewizard.resume.Step
import scalatags.Text.all.*

case class ResumeContactRoutes(databaseResource: DatabaseResource)(implicit cc: castor.Context, log: cask.Logger) extends cask.Routes:
  val dslContext = databaseResource.ctx

  @cask.get("/wizard/contact")
  def getWizardContact() =
    if dslContext.fetchCount(RESUME_DETAILS) > 0 then
      val result = dslContext.fetchOne(RESUME_DETAILS)
      val form = ResumePageView.buildForm(Step.Contact, buildContactsForm(result.getPhone(), result.getEmail(), result.getLocation()))
      ResumePageView.buildPage(ResumePageView.buildSteps(Step.Contact), form)
    else
      val form = ResumePageView.buildForm(Step.Contact, buildContactsForm("", "", ""))
      ResumePageView.buildPage(ResumePageView.buildSteps(Step.Contact), form) 

  @cask.postForm("/wizard/contact")
  def postWizardContact(phone: String, email: String, location: String) =
    if dslContext.fetchCount(RESUME_DETAILS) > 0 then
      val resumeDetail = dslContext.selectFrom(RESUME_DETAILS).fetchOne()
      dslContext.update(RESUME_DETAILS)
        .set(RESUME_DETAILS.PHONE, phone)
        .set(RESUME_DETAILS.EMAIL, email)
        .set(RESUME_DETAILS.LOCATION, location)
        .execute()
    else
      dslContext.insertInto(RESUME_DETAILS, RESUME_DETAILS.PHONE, RESUME_DETAILS.EMAIL, RESUME_DETAILS.LOCATION)
        .values(phone, email, location)
        .execute()
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
