package net.sailware.resumewizard.resume.wizard

import net.sailware.resumewizard.resume.ResumeCertificationsRepository
import net.sailware.resumewizard.resume.ResumePageView
import net.sailware.resumewizard.resume.Step
import scalatags.Text.all.*

case class ResumeCertificationRoutes(repository: ResumeCertificationsRepository)(implicit cc: castor.Context, log: cask.Logger) extends cask.Routes:

  @cask.get("/wizard/certification")
  def getWizardCertification() =
    if repository.fetchCount() > 0 then
      val result = repository.fetchOne()
      val formContent = buildCertificationForm(result.getTitle(), result.getOrganization(), result.getYear(), result.getLocation())
      ResumePageView.view(Step.Certification, formContent)
    else
      val formContent = buildCertificationForm("", "", "", "")
      ResumePageView.view(Step.Certification, formContent)

  @cask.postForm("/wizard/certification")
  def postWizardCertification(title: String, organization: String, year: String, location: String) =
    if repository.fetchCount() > 0 then
      val result = repository.fetchOne()
      repository.update(result.getId(), title, organization, year, location)
    else
      repository.insert(title, organization, year, location)
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
