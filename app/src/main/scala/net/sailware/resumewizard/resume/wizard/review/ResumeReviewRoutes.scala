package net.sailware.resumewizard.resume.wizard.review

import net.sailware.resumewizard.resume.Certification
import net.sailware.resumewizard.resume.Detail
import net.sailware.resumewizard.resume.Experience
import net.sailware.resumewizard.resume.Skill
import net.sailware.resumewizard.resume.Social
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
    ResumePageView.view(
      Step.Review,
      buildReview(detailsOption, socials, experiences, skills, certifications)
    )

  def buildReview(detailsOption: Option[Detail], socials: List[Social], experiences: List[Experience], skills: List[Skill], certifications: List[Certification]) =
    val details = List(
        p(strong("Name: "), detailsOption.get.name),
        p(strong("Title: "), detailsOption.get.title),
        p(strong("Summary: "), detailsOption.get.summary),
        p(strong("Phone: "), detailsOption.get.phone),
        p(strong("Email: "), detailsOption.get.email),
        p(strong("Location: "), detailsOption.get.location),
    )

    val socialTags = socials.map(social =>
      List(
        p(strong(s"Social[${social.id}] Name: "), social.name),
        p(strong(s"Social[${social.id}] URL: "), social.url),
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
          p(strong(s"Skill[${skill.id}] Name: "), skill.name),
          p(strong(s"Skill[${skill.id}] Rating: "), skill.rating.toString),
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

    div(cls := "page")(
      div(cls := "page__content")(
        // Section - Name
        div(cls := "section")(
          div()(
            if detailsOption.isDefined then detailsOption.get.name else frag()
          )
        ),
        // Section - Title
        div(cls := "section")(
          div()(
            if detailsOption.isDefined then detailsOption.get.title else frag()
          )
        ),
        // Section - Contact Info
        div(cls := "section")(
          div()(
            if detailsOption.isDefined then List(detailsOption.get.phone, detailsOption.get.email, detailsOption.get.location).mkString(" | ") else frag()
          )
        ),
        // Section - Socials
        div(cls := "section")(
          div()(
            socials.map(social => social.url).mkString(" | ")
          )
        ),
        // Section - Header Summary
        div(cls := "section")(
          div()("Summary")
        ),
        // Section - Summary
        div(cls := "section")(
          div()(
            if detailsOption.isDefined then detailsOption.get.summary else frag()
          )
        ),
        // Section - Header Skills
        div(cls := "section")(
          div()("Skills")
        ),
        // Section - Skills
        div(cls := "section")(
          div()(
            skills.map(skill => skill.name).mkString(" | ")
          )
        ),
        // Section - Header Experience
        div(cls := "section")(
          div()("Experience")
        ),
        // Section - Experience
        div(cls := "section")(
          for experience <- experiences yield
            List(
              div()(experience.title),
              div()(List(experience.organization, experience.location, experience.duration).mkString(" | ")),
              ul()(
                for description <- experience.description.split("\n") yield
                  li()(description)
              )
            )
        ),
        // Section - Header Certification
        div(cls := "section")(
          div()("Certification")
        ),
        // Section - Header
        div(cls := "section")(
          for certification <- certifications yield
            List(
              div()(certification.title),
              div()(List(certification.organization, certification.location, certification.year).mkString(" | "))
            )
        )
      )
    )

  initialize()
