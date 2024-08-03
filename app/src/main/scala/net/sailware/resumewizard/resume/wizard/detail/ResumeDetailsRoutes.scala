package net.sailware.resumewizard.resume.wizard.detail

import net.sailware.resumewizard.resume.Detail
import net.sailware.resumewizard.resume.ResumeDetailsRepository
import net.sailware.resumewizard.resume.Step
import net.sailware.resumewizard.resume.wizard.detail.view.ResumeDetailView
import net.sailware.resumewizard.resume.wizard.detail.view.model.DetailForm
import net.sailware.resumewizard.resume.wizard.detail.view.model.DetailViewRequest

case class ResumeDetailsRoutes(repository: ResumeDetailsRepository)(implicit cc: castor.Context, log: cask.Logger) extends cask.Routes:

  @cask.get("/wizard/detail")
  def getWizardDetail() =
    if repository.fetchCount() > 0 then
      val detail = repository.fetchOne()
      ResumeDetailView.view(DetailViewRequest(Step.Detail, detail))
    else
      ResumeDetailView.view(DetailViewRequest(Step.Detail, Detail()))

  @cask.post("/wizard/detail")
  def postWizardDetail(request: cask.Request) =
    val form = DetailForm(request)
    if repository.fetchCount() > 0 then
      val result = repository.fetchOne()
      repository.update(result.id, form.name, form.title, form.summary, form.phone, form.email, form.location)
    else
      repository.insert(form.name, form.title, form.summary, form.phone, form.email, form.location)
    cask.Redirect("/wizard/social")

  initialize()



