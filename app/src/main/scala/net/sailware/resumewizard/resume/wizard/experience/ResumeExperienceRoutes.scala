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
      val experiences = repository.fetch().map(experience =>
        Experience(
          experience.getId(),
          experience.getTitle(),
          experience.getOrganization(),
          experience.getDuration(),
          experience.getLocation(),
          experience.getDescription(),
          experience.getSkills()
        )
      )
      ResumeExperienceView.view(ExperienceViewRequest(Step.Experience, experiences))
    else
      ResumeExperienceView.view(ExperienceViewRequest(Step.Experience, List.empty))

  @cask.post("/wizard/experience")
  def postWizardExperience(request: cask.Request) =
    // build form from request
    val form = ExperienceListForm(request)

    // delete removed values
    val experienceIds = form.entries.map(_.id)
    repository.deleteByExcludedIds(experienceIds)

    // update values
    form.entries.foreach(entry => repository.update(entry.id, entry.title, entry.organization, entry.duration, entry.location, entry.description, entry.skills))

    // insert new values
    form.newEntries.foreach(entry => repository.insert(entry.title, entry.organization, entry.duration, entry.location, entry.description, entry.skills))

    cask.Redirect("/wizard/skill")

  initialize()
