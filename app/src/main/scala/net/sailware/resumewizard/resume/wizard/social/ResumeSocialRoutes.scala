package net.sailware.resumewizard.resume.wizard.social

import net.sailware.resumewizard.resume.ResumeSocialsRepository
import net.sailware.resumewizard.resume.Step
import net.sailware.resumewizard.resume.wizard.social.form.SocialListForm
import net.sailware.resumewizard.resume.wizard.social.view.ResumeSocialView
import net.sailware.resumewizard.resume.wizard.social.view.model.SocialViewRequest
import net.sailware.resumewizard.resume.wizard.social.view.model.Social

import scalatags.Text.all.*

case class ResumeSocialRoutes(repository: ResumeSocialsRepository)(implicit cc: castor.Context, log: cask.Logger) extends cask.Routes:

  @cask.get("/wizard/social")
  def getWizardSocial() =
    if repository.fetchCount() > 0 then
      val socials = repository.fetch()
        .map(social => Social(social.getId(), social.getName(), social.getUrl()))
      ResumeSocialView.view(SocialViewRequest(Step.Social, socials))
    else
      ResumeSocialView.view(SocialViewRequest(Step.Social, List.empty))

  @cask.post("/wizard/social")
  def postWizardSocial(request: cask.Request) =
    // build form from request
    val form = SocialListForm(request)

    // delete removed values
    repository.deleteByExcludedIds(form.entries.map(_.id))

    // update values
    form.entries.foreach(social => repository.update(social.id, social.name, social.url))

    // insert new values
    form.newEntries.foreach(social => repository.insert(social.name, social.url))

    cask.Redirect("/wizard/experience")

  initialize()


