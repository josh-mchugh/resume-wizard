package net.sailware.resumewizard.resume.wizard.skill

import net.sailware.resumewizard.resume.ResumeSkillsRepository
import net.sailware.resumewizard.resume.Step
import net.sailware.resumewizard.resume.wizard.skill.view.ResumeSkillView
import net.sailware.resumewizard.resume.wizard.skill.view.model.SkillViewRequest
import net.sailware.resumewizard.resume.wizard.skill.view.model.SkillEntryListForm

case class ResumeSkillRoutes(repository: ResumeSkillsRepository)(implicit cc: castor.Context, log: cask.Logger) extends cask.Routes:

  @cask.get("/wizard/skill")
  def getWizardSkill() =
    if repository.fetchCount() > 0 then
      val skills = repository.fetch()
      ResumeSkillView.view(SkillViewRequest(Step.Skill, skills))
    else
      ResumeSkillView.view(SkillViewRequest(Step.Skill, List.empty))

  @cask.post("/wizard/skill")
  def postWizardSkill(request: cask.Request) =
    val form = SkillEntryListForm(request)

    repository.deleteByExcludedIds(form.entries.map(_.id))

    form.entries.foreach(skill => repository.update(skill.id, skill.name, skill.rating))

    form.newEntries.foreach(skill => repository.insert(skill.name, skill.rating))

    cask.Redirect("/wizard/certification")

  initialize()
