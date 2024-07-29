package net.sailware.resumewizard.resume.wizard.social

import net.sailware.resumewizard.resume.ResumeSocialsRepository
import net.sailware.resumewizard.resume.Step
import net.sailware.resumewizard.resume.wizard.social.ResumeSocialFormUtil
import net.sailware.resumewizard.resume.wizard.social.view.ResumeSocialView
import net.sailware.resumewizard.resume.wizard.social.view.model.SocialViewRequest
import net.sailware.resumewizard.resume.wizard.social.view.model.Social

import scalatags.Text.all.*

case class ResumeSocialRoutes(repository: ResumeSocialsRepository)(implicit cc: castor.Context, log: cask.Logger) extends cask.Routes:

  @cask.get("/wizard/social")
  def getWizardSocial() =
    val results = repository.fetch()
     if results.nonEmpty then
      // TODO: Create a model object for the view of Social
      ResumeSocialView.view(SocialViewRequest(Step.Social, results.map(social => Social(social.getId(), social.getName(), social.getUrl()))))
    else
      ResumeSocialView.view(SocialViewRequest(Step.Social, List.empty))

  @cask.post("/wizard/social")
  def postWizardSocial(request: cask.Request) =
    // TODO: can ResumeSocialFormUtil just be SocialListForm with a companion object?
    val form = ResumeSocialFormUtil.bind(request)

    // delete removed values
    repository.deleteByExcludedIds(form.entries.map(_.id))

    // update values
    form.entries.foreach(social => repository.update(social.id, social.name, social.url))

    // insert new values
    form.newEntries.foreach(social => repository.insert(social.name, social.url))

    cask.Redirect("/wizard/experience")

  initialize()


