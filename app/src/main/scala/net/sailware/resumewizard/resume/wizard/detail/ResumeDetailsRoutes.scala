package net.sailware.resumewizard.resume.wizard.detail

import net.sailware.resumewizard.resume.ResumeDetailsRepository
import net.sailware.resumewizard.resume.Step
import net.sailware.resumewizard.resume.wizard.detail.view.ResumeDetailView
import net.sailware.resumewizard.resume.wizard.detail.view.model.Detail
import net.sailware.resumewizard.resume.wizard.detail.view.model.DetailViewRequest

case class ResumeDetailsRoutes(repository: ResumeDetailsRepository)(implicit cc: castor.Context, log: cask.Logger) extends cask.Routes:

  @cask.get("/wizard/detail")
  def getWizardName() =
    if repository.fetchCount() > 0 then
      val record= repository.fetchOne()
      val detail = Detail(record)
      ResumeDetailView.view(DetailViewRequest(Step.Detail, detail))
    else
      ResumeDetailView.view(DetailViewRequest(Step.Detail, Detail()))

  @cask.postForm("/wizard/detail")
  def postWizardName(name: String, title: String, summary: String, phone: String, email: String, location: String) =
    if repository.fetchCount() > 0 then
      val result = repository.fetchOne()
      repository.update(result.getId(), name, title, summary, phone, email, location)
    else
      repository.insert(name, title, summary, phone, email, location)
    cask.Redirect("/wizard/social")

  initialize()



