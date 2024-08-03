package net.sailware.resumewizard.resume.wizard.social

import net.sailware.resumewizard.resume.ResumeSocialsRepository
import net.sailware.resumewizard.resume.Step
import net.sailware.resumewizard.resume.wizard.social.view.ResumeSocialView
import net.sailware.resumewizard.resume.wizard.social.view.model.SocialViewRequest
import net.sailware.resumewizard.resume.wizard.social.view.model.SocialEntryListForm

case class ResumeSocialRoutes(repository: ResumeSocialsRepository)(implicit cc: castor.Context, log: cask.Logger) extends cask.Routes:

  @cask.get("/wizard/social")
  def getWizardSocial() =
    if repository.fetchCount() > 0 then
      val socials = repository.fetch()
      ResumeSocialView.view(SocialViewRequest(Step.Social, socials))
    else
      ResumeSocialView.view(SocialViewRequest(Step.Social, List.empty))

  @cask.post("/wizard/social")
  def postWizardSocial(request: cask.Request) =
    val form = SocialEntryListForm(request)

    repository.deleteByExcludedIds(form.entries.map(_.id))

    form.entries.foreach(social => repository.update(social.id, social.name, social.url))

    form.newEntries.foreach(social => repository.insert(social.name, social.url))

    cask.Redirect("/wizard/experience")

  initialize()


