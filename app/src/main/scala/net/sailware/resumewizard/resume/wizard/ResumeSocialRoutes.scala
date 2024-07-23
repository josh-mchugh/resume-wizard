package net.sailware.resumewizard.resume.wizard

import net.sailware.resumewizard.resume.wizard.ResumeSocialFormUtil
import net.sailware.resumewizard.resume.ResumeSocialsRepository
import net.sailware.resumewizard.resume.ResumePageView
import net.sailware.resumewizard.resume.Step
import scalatags.Text.all.*

case class ResumeSocialRoutes(repository: ResumeSocialsRepository)(implicit cc: castor.Context, log: cask.Logger) extends cask.Routes:

  @cask.get("/wizard/social")
  def getWizardSocial() =
    if repository.fetchCount() > 0 then
      val result = repository.fetchOne()
      val formContent = buildSocialsForm(result.getName(), result.getUrl())
      ResumePageView.view(Step.Social, formContent)
    else
      val formContent = buildSocialsForm("", "")
      ResumePageView.view(Step.Social, formContent) 

  @cask.post("/wizard/social")
  def postWizardSocial(request: cask.Request) =
    val form = ResumeSocialFormUtil.bind(request)
    if repository.fetchCount() > 0 then
      val result = repository.fetchOne()
      repository.update(result.getId(), form.socials(0).name, form.socials(0).url)
    else
      repository.insert(form.socials(0).name, form.socials(0).url)
    cask.Redirect("/wizard/experience")

  def buildSocialsForm(socialName: String, url: String) =
    List(
      div()(
        label(cls := "form-label")("Name"),
        input(cls := "form-control", `type` := "text", name := "form[0].name", placeholder := "Name", value := socialName)
      ),
      div(cls := "mt-3")(
        label(cls := "form-label")("URL"),
        input(cls := "form-control", `type` := "text", name := "form[0].url", placeholder := "URL", value := url)
      ),
      hr(),
      div(cls := "mt-3")(
        label(cls := "form-label")("Name"),
        input(cls := "form-control", `type` := "text", name := "form[1].name", placeholder := "Name", value := socialName)
      ),
      div(cls := "mt-3")(
        label(cls := "form-label")("URL"),
        input(cls := "form-control", `type` := "text", name := "form[1].url", placeholder := "URL", value := url)
      ),
    )


  initialize()
