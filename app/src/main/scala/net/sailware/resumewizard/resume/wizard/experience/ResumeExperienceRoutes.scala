package net.sailware.resumewizard.resume.wizard.social

import net.sailware.resumewizard.resume.ResumeExperiencesRepository
import net.sailware.resumewizard.resume.Step
import net.sailware.resumewizard.resume.wizard.experience.form.ExperienceListForm
import net.sailware.resumewizard.resume.wizard.experience.view.ResumeExperienceView
import net.sailware.resumewizard.resume.wizard.experience.view.model.Experience
import net.sailware.resumewizard.resume.wizard.experience.view.model.ExperienceViewRequest
import scalatags.Text.all.*

case class ResumeExperienceRoutes(repository: ResumeExperiencesRepository)(implicit cc: castor.Context, log: cask.Logger) extends cask.Routes:

  @cask.get("/wizard/experience")
  def getWizardExperience() =
    if repository.fetchCount() > 0 then
      val result = repository.fetchOne()
      val experience = Experience(
        result.getId(),
        result.getTitle(),
        result.getOrganization(),
        result.getDuration(),
        result.getLocation(),
        result.getDescription(),
        result.getSkills()
      )
      ResumeExperienceView.view(ExperienceViewRequest(Step.Experience, List(experience)))
    else
      ResumeExperienceView.view(ExperienceViewRequest(Step.Experience, List.empty))

  @cask.post("/wizard/experience")
  def postWizardExperience(request: cask.Request) =
    // build form from request
    val form = ExperienceListForm(request)
    println(form)
    /*if repository.fetchCount() > 0 then
      val result = repository.fetchOne()
      repository.update(result.getId(), title, organization, duration, location, description, skills)
    else
      repository.insert(title, organization, duration, location, description, skills)
     */
    cask.Redirect("/wizard/skill")

  initialize()
