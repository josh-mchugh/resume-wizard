package net.sailware.resumewizard.resume.wizard

import io.undertow.server.handlers.form.FormParserFactory
import net.sailware.resumewizard.database.DatabaseResource
import net.sailware.resumewizard.jooq.Tables.RESUME_SOCIALS
import net.sailware.resumewizard.resume.ResumePageView
import net.sailware.resumewizard.resume.Step
import scalatags.Text.all.*

import scala.collection.JavaConverters.asScalaIteratorConverter

case class ResumeSocialRoutes(databaseResource: DatabaseResource)(implicit cc: castor.Context, log: cask.Logger) extends cask.Routes:
  val dslContext = databaseResource.ctx

  @cask.get("/wizard/social")
  def getWizardSocial() =
    if dslContext.fetchCount(RESUME_SOCIALS) > 0 then
      val result = dslContext.fetchOne(RESUME_SOCIALS)
      val form = ResumePageView.buildForm(Step.Social, buildSocialsForm(result.getName(), result.getUrl()))
      ResumePageView.buildPage(ResumePageView.buildSteps(Step.Social), form)
    else
      val form = ResumePageView.buildForm(Step.Social, buildSocialsForm("", ""))
      ResumePageView.buildPage(ResumePageView.buildSteps(Step.Social), form) 

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

    if dslContext.fetchCount(RESUME_SOCIALS) > 0 then
      val resumeDetail = dslContext.selectFrom(RESUME_SOCIALS).fetchOne()
      dslContext.update(RESUME_SOCIALS)
        .set(RESUME_SOCIALS.NAME, data("0").name)
        .set(RESUME_SOCIALS.URL, data("0").url)
        .execute()
    else
      dslContext.insertInto(RESUME_SOCIALS, RESUME_SOCIALS.NAME, RESUME_SOCIALS.URL)
        .values(data("0").name, data("0").url)
        .execute()
 
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
