package net.sailware.resumewizard.resume.wizard

import net.sailware.resumewizard.resume.ResumeSkillsRepository
import net.sailware.resumewizard.resume.ResumePageView
import net.sailware.resumewizard.resume.Step
import scalatags.Text.all.*

case class ResumeSkillRoutes(repository: ResumeSkillsRepository)(implicit cc: castor.Context, log: cask.Logger) extends cask.Routes:

  @cask.get("/wizard/skill")
  def getWizardSkill() =
    if repository.fetchCount() > 0 then
      val result = repository.fetchOne()
      val formContent = buildSkillForm(result.getName(), result.getRating())
      ResumePageView.view(Step.Skill, formContent)
    else
      val formContent = buildSkillForm("", 0)
      ResumePageView.view(Step.Skill, formContent) 

  @cask.postForm("/wizard/skill")
  def postWizardSkill(name: String, rating: Short) =
    if repository.fetchCount() > 0 then
      val result = repository.fetchOne()
      repository.update(result.getId(), name, rating)
    else
      repository.insert(name, rating)
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
