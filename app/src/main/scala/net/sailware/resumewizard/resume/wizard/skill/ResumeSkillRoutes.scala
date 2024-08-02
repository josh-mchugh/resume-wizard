package net.sailware.resumewizard.resume.wizard

import net.sailware.resumewizard.resume.ResumeSkillsRepository
import net.sailware.resumewizard.resume.Step
import net.sailware.resumewizard.resume.wizard.skill.view.ResumeSkillView
import net.sailware.resumewizard.resume.wizard.skill.view.model.Skill
import net.sailware.resumewizard.resume.wizard.skill.view.model.SkillViewRequest
import net.sailware.resumewizard.resume.wizard.skill.view.model.SkillEntryListForm

case class ResumeSkillRoutes(repository: ResumeSkillsRepository)(implicit cc: castor.Context, log: cask.Logger) extends cask.Routes:

  @cask.get("/wizard/skill")
  def getWizardSkill() =
    if repository.fetchCount() > 0 then
      val skills = repository.fetch().map(record => Skill(record))
      ResumeSkillView.view(SkillViewRequest(Step.Skill, skills))
    else
      ResumeSkillView.view(SkillViewRequest(Step.Skill, List.empty))

  @cask.post("/wizard/skill")
  def postWizardSkill(request: cask.Request) =
    // build form from request
    val form = SkillEntryListForm(request)

    // delete removed values
    repository.deleteByExcludedIds(form.entries.map(_.id))

    // update values
    form.entries.foreach(skill => repository.update(skill.id, skill.name, skill.rating))

    // insert new values
    form.newEntries.foreach(skill => repository.insert(skill.name, skill.rating))

    cask.Redirect("/wizard/certification")

  initialize()
