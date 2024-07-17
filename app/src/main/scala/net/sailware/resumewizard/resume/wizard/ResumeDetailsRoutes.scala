package net.sailware.resumewizard.resume.wizard

import scalatags.Text.all.*
import scalatags.Text.tags2.nav
import scalatags.Text.tags2.section
import scalatags.Text.tags2.title
import net.sailware.resumewizard.database.DatabaseResource
import net.sailware.resumewizard.jooq.Tables.RESUME_DETAILS
import net.sailware.resumewizard.resume.ResumePageView
import net.sailware.resumewizard.resume.Step

case class ResumeDetailsRoutes(databaseResource: DatabaseResource)(implicit cc: castor.Context, log: cask.Logger) extends cask.Routes:

  val dslContext = databaseResource.ctx

  @cask.get("/wizard/detail")
  def getWizardName() =
    if dslContext.fetchCount(RESUME_DETAILS) > 0 then
      val result = dslContext.fetchOne(RESUME_DETAILS)
      val form = ResumePageView.buildForm(Step.Detail, buildNameAndTitleForm(result.getName(), result.getTitle(), result.getSummary()))
      ResumePageView.buildPage(ResumePageView.buildSteps(Step.Detail), form)
    else
      val form = ResumePageView.buildForm(Step.Detail, buildNameAndTitleForm("", "", ""))
      ResumePageView.buildPage(ResumePageView.buildSteps(Step.Detail), form) 

  @cask.postForm("/wizard/detail")
  def postWizardName(name: String, title: String, summary: String) =
    if dslContext.fetchCount(RESUME_DETAILS) > 0 then
      val resumeDetail = dslContext.selectFrom(RESUME_DETAILS).fetchOne()
      dslContext.update(RESUME_DETAILS)
        .set(RESUME_DETAILS.NAME, name)
        .set(RESUME_DETAILS.TITLE, title)
        .set(RESUME_DETAILS.SUMMARY, summary)
        .execute()
    else
      dslContext.insertInto(RESUME_DETAILS, RESUME_DETAILS.NAME, RESUME_DETAILS.TITLE, RESUME_DETAILS.SUMMARY)
        .values(name, title, summary)
        .execute()
    cask.Redirect("/wizard/contact")

  def buildNameAndTitleForm(resumeName: String, title: String, summary: String) =
    List(
      div()(
        label(cls := "form-label")("Name"),
        input(cls := "form-control", `type` := "text", name := "name", placeholder := "Name", value := resumeName)
      ),
      div(cls := "mt-3")(
        label(cls := "form-label")("Title"),
        input(cls := "form-control", `type` := "text", name := "title", placeholder := "Title", value := title)
      ),
      div(cls := "mt-3")(
        label(cls := "form-label")("Summary"),
        textarea(cls := "form-control", rows := 3, name := "summary",  placeholder := "Summary of your current or previous role")(summary)
      )
    )

  initialize()



