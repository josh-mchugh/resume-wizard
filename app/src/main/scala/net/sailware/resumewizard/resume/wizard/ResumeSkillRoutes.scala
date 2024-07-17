package net.sailware.resumewizard.resume.wizard

import net.sailware.resumewizard.database.DatabaseResource
import net.sailware.resumewizard.jooq.Tables.RESUME_SKILLS
import net.sailware.resumewizard.resume.ResumePageView
import net.sailware.resumewizard.resume.Step
import scalatags.Text.all.*

case class ResumeSkillRoutes(databaseResource: DatabaseResource)(implicit cc: castor.Context, log: cask.Logger) extends cask.Routes:
  val dslContext = databaseResource.ctx

  @cask.get("/wizard/skill")
  def getWizardSkill() =
    if dslContext.fetchCount(RESUME_SKILLS) > 0 then
      val result = dslContext.fetchOne(RESUME_SKILLS)
      val form = ResumePageView.buildForm(Step.Skill, buildSkillForm(result.getName(), result.getRating()))
      ResumePageView.buildPage(ResumePageView.buildSteps(Step.Skill), form)
    else
      val form = ResumePageView.buildForm(Step.Skill, buildSkillForm("", 0))
      ResumePageView.buildPage(ResumePageView.buildSteps(Step.Skill), form) 

  @cask.postForm("/wizard/skill")
  def postWizardSkill(name: String, rating: Short) =
    if dslContext.fetchCount(RESUME_SKILLS) > 0 then
      val resumeDetail = dslContext.selectFrom(RESUME_SKILLS).fetchOne()
      dslContext.update(RESUME_SKILLS)
        .set(RESUME_SKILLS.NAME, name)
        .set(RESUME_SKILLS.RATING, rating)
        .execute()
    else
      dslContext.insertInto(RESUME_SKILLS, RESUME_SKILLS.NAME, RESUME_SKILLS.RATING)
        .values(name, rating)
        .execute()
    cask.Redirect("/wizard/certification")

  def buildSkillForm(skillName: String, rating: Short) =
    List(
      div()(
        label(cls := "form-label")("Name"),
        input(cls := "form-control", `type` := "text", name := "name", placeholder := "Name", value := skillName)
      ),
      div(cls := "mt-3")(
        label(cls := "form-label")("Rating"),
        input(cls := "form-range", `type` := "range", name := "rating", min := 0, max := 5, step := 1, value := rating)
      )
    )

  initialize()
