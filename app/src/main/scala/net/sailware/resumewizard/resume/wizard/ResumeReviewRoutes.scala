package net.sailware.resumewizard.resume.wizard

import net.sailware.resumewizard.resume.Certification
import net.sailware.resumewizard.resume.Experience
import net.sailware.resumewizard.resume.ResumeCertificationsRepository
import net.sailware.resumewizard.resume.ResumeDetailsRepository
import net.sailware.resumewizard.resume.ResumeExperiencesRepository
import net.sailware.resumewizard.resume.ResumeSkillsRepository
import net.sailware.resumewizard.resume.ResumeSocialsRepository
import net.sailware.resumewizard.jooq.tables.records.ResumeCertificationsRecord
import net.sailware.resumewizard.jooq.tables.records.ResumeDetailsRecord
import net.sailware.resumewizard.jooq.tables.records.ResumeExperiencesRecord
import net.sailware.resumewizard.jooq.tables.records.ResumeSkillsRecord
import net.sailware.resumewizard.jooq.tables.records.ResumeSocialsRecord
import net.sailware.resumewizard.resume.ResumePageView
import net.sailware.resumewizard.resume.Step
import scalatags.Text.all.*

case class ResumeReviewRoutes(
  certificationsRepository: ResumeCertificationsRepository,
  detailsRepository: ResumeDetailsRepository,
  experiencesRepository: ResumeExperiencesRepository,
  skillsRepository: ResumeSkillsRepository,
  socialsRepository: ResumeSocialsRepository,
)(implicit cc: castor.Context, log: cask.Logger) extends cask.Routes:

  @cask.get("/wizard/review")
  def getWizardReview() =
    val detailsOption = detailsRepository.fetchOption()
    val socials = socialsRepository.fetch()
    val experiences = experiencesRepository.fetch()
    val skills = skillsRepository.fetch()
    val certifications = certificationsRepository.fetch()
    ResumePageView.view(Step.Review, buildReview(detailsOption, socials, experiences, skills, certifications))

  def buildReview(detailsOption: Option[ResumeDetailsRecord], socials: List[ResumeSocialsRecord], experiences: List[Experience], skills: List[ResumeSkillsRecord], certifications: List[Certification]) =
    val details = List(
        p(strong("Name: "), detailsOption.get.getName()),
        p(strong("Title: "), detailsOption.get.getTitle()),
        p(strong("Summary: "), detailsOption.get.getSummary()),
        p(strong("Phone: "), detailsOption.get.getPhone()),
        p(strong("Email: "), detailsOption.get.getEmail()),
        p(strong("Location: "), detailsOption.get.getLocation()),
    )

    val socialTags = socials.map(social =>
      List(
        p(strong(s"Social[${social.getId()}] Name: "), social.getName()),
        p(strong(s"Social[${social.getId()}] URL: "), social.getUrl()),
      )
    )

    val experienceTags = experiences.map(experience =>
      List(
        p(strong(s"Experience[${experience.id}] Title: "), experience.title),
        p(strong(s"Experience[${experience.id}] Organization: "), experience.organization),
        p(strong(s"Experience[${experience.id}] Duration: "), experience.duration),
        p(strong(s"Experience[${experience.id}] Location: "), experience.location),
        p(strong(s"Experience[${experience.id}] Description: "), experience.description),
        p(strong(s"Experience[${experience.id}] Skills: "), experience.skills),
      )
    )

    val skillTags = skills.map(skill =>
      List(
          p(strong(s"Skill[${skill.getId()}] Name: "), skill.getName()),
          p(strong(s"Skill[${skill.getId()}] Rating: "), skill.getRating().toString()),
      )
    )

    val certificationTags = certifications.map(certification =>
      List(
        p(strong(s"Certification[${certification.id}] Title: "), certification.title),
        p(strong(s"Certification[${certification.id}] Organization: "), certification.organization),
        p(strong(s"Certification[${certification.id}] Year: "), certification.year),
        p(strong(s"Certification[${certification.id}] Location: "), certification.location),
      )
    )

    div()(
      if detailsOption.isDefined then details else frag(),
      if socialTags.nonEmpty then socialTags else frag(),
      if experienceTags.nonEmpty then experienceTags else frag(),
      if skills.nonEmpty then skillTags else frag(),
      if certifications.nonEmpty then certificationTags else frag()
    )

  initialize()
