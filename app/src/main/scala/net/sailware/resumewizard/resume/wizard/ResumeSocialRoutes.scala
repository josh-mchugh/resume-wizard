package net.sailware.resumewizard.resume.wizard

import io.undertow.server.handlers.form.FormParserFactory
import net.sailware.resumewizard.resume.ResumeSocialsRepository
import net.sailware.resumewizard.resume.ResumePageView
import net.sailware.resumewizard.resume.Step
import scalatags.Text.all.*

import scala.collection.JavaConverters.asScalaIteratorConverter

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

  case class SocialForm(val name: String, val url: String)

  @cask.post("/wizard/social")
  def postWizardSocial(request: cask.Request) =
    val formData = FormParserFactory.builder().build().createParser(request.exchange).parseBlocking()
    var data = Map.empty[String, SocialForm]
    for key <- formData.iterator().asScala do
      data = key match
        case s"form[$i].$variable" =>
          if data.contains(i) then
            variable match
              case "name" => data + (i -> data(i).copy(name = formData.get(key).element().getValue()))
              case "url" => data + (i -> data(i).copy(url = formData.get(key).element().getValue()))
              case _ => data
          else
            variable match
              case "name" => data + (i -> SocialForm(formData.get(key).element().getValue(), ""))
              case "url" => data + (i -> SocialForm("", formData.get(key).element().getValue()))
              case _ => data
        case _ => data

    if repository.fetchCount() > 0 then
      val result = repository.fetchOne()
      repository.update(result.getId(), data("0").name, data("0").url)
    else
      repository.insert(data("0").name, data("0").url)
 
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
