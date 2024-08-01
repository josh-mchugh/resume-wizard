package net.sailware.resumewizard.resume.wizard

import net.sailware.resumewizard.resume.ResumeSkillsRepository
import net.sailware.resumewizard.resume.Step
import net.sailware.resumewizard.resume.wizard.skill.view.ResumeSkillView
import net.sailware.resumewizard.resume.wizard.skill.view.model.Skill
import net.sailware.resumewizard.resume.wizard.skill.view.model.SkillViewRequest

case class ResumeSkillRoutes(repository: ResumeSkillsRepository)(implicit cc: castor.Context, log: cask.Logger) extends cask.Routes:

  @cask.get("/wizard/skill")
  def getWizardSkill() =
    if repository.fetchCount() > 0 then
      val skills = repository.fetch().map(record => Skill(record))
      ResumeSkillView.view(SkillViewRequest(Step.Skill, skills))
    else
      ResumeSkillView.view(SkillViewRequest(Step.Skill, List.empty))

  @cask.postForm("/wizard/skill")
  def postWizardSkill(name: String, rating: Short) =
    if repository.fetchCount() > 0 then
      val result = repository.fetchOne()
      repository.update(result.getId(), name, rating)
    else
      repository.insert(name, rating)
    cask.Redirect("/wizard/certification")

  initialize()
