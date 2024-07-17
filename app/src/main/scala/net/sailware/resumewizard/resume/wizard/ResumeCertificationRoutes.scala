package net.sailware.resumewizard.resume.wizard

import net.sailware.resumewizard.database.DatabaseResource
import net.sailware.resumewizard.jooq.Tables.RESUME_CERTIFICATIONS
import net.sailware.resumewizard.resume.ResumePageView
import net.sailware.resumewizard.resume.Step
import scalatags.Text.all.*

case class ResumeCertificationRoutes(databaseResource: DatabaseResource)(implicit cc: castor.Context, log: cask.Logger) extends cask.Routes:
  val dslContext = databaseResource.ctx

  @cask.get("/wizard/certification")
  def getWizardCertification() =
    if dslContext.fetchCount(RESUME_CERTIFICATIONS) > 0 then
      val result = dslContext.fetchOne(RESUME_CERTIFICATIONS)
      val form = ResumePageView.buildForm(Step.Certification, buildCertificationForm(result.getTitle(), result.getOrganization(), result.getYear(), result.getLocation()))
      ResumePageView.buildPage(ResumePageView.buildSteps(Step.Certification), form)
    else
      val form = ResumePageView.buildForm(Step.Certification, buildCertificationForm("", "", "", ""))
      ResumePageView.buildPage(ResumePageView.buildSteps(Step.Certification), form)

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

  initialize()
