package net.sailware.resumewizard.resume.wizard

import net.sailware.resumewizard.resume.ResumeSocialsRepository
import net.sailware.resumewizard.resume.Step
import net.sailware.resumewizard.resume.wizard.ResumeSocialFormUtil
import net.sailware.resumewizard.resume.wizard.ResumeSocialView

import scalatags.Text.all.*

case class ResumeSocialRoutes(repository: ResumeSocialsRepository)(implicit cc: castor.Context, log: cask.Logger) extends cask.Routes:

  @cask.get("/wizard/social")
  def getWizardSocial() =
    val results = repository.fetch()
    if results.nonEmpty then
      ResumeSocialView.view(SocialViewRequest(Step.Social, results.map(social => (social.getId(), social.getName(), social.getUrl()))))
    else
      ResumeSocialView.view(SocialViewRequest(Step.Social, List.empty))

  @cask.post("/wizard/social")
  def postWizardSocial(request: cask.Request) =
    val form = ResumeSocialFormUtil.bind(request)
    if repository.fetchCount() > 0 then
      val result = repository.fetchOne()
      repository.update(result.getId(), form.socials(0).name, form.socials(0).url)
    else
      repository.insert(form.socials(0).name, form.socials(0).url)
    cask.Redirect("/wizard/experience")

  initialize()
