package net.sailware.resumewizard.resume.wizard.certification

import net.sailware.resumewizard.resume.ResumeCertificationsRepository
import net.sailware.resumewizard.resume.Step
import net.sailware.resumewizard.resume.wizard.certification.view.ResumeCertificationView
import net.sailware.resumewizard.resume.wizard.certification.view.model.CertificationViewRequest
import net.sailware.resumewizard.resume.wizard.certification.view.model.CertificationEntryListForm

case class ResumeCertificationRoutes(repository: ResumeCertificationsRepository)(implicit cc: castor.Context, log: cask.Logger) extends cask.Routes:

  @cask.get("/wizard/certification")
  def getWizardCertification() =
    if repository.fetchCount() > 0 then
      val certifications = repository.fetch()
      ResumeCertificationView.view(CertificationViewRequest(Step.Certification, certifications))
    else
      ResumeCertificationView.view(CertificationViewRequest(Step.Certification, List.empty))

  @cask.post("/wizard/certification")
  def postWizardCertification(request: cask.Request) =
    val form = CertificationEntryListForm(request)

    repository.deleteByExcludedIds(form.entries.map(_.id))

    form.entries.foreach(certification => repository.update(certification.id, certification.title, certification.organization, certification.year, certification.location))

    form.newEntries.foreach(certification => repository.insert(certification.title, certification.organization, certification.year, certification.location))

    cask.Redirect("/wizard/review")

  initialize()
