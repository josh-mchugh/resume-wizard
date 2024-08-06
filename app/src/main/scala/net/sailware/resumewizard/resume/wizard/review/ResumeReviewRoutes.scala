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

    div(cls := "page")(
      div(cls := "page__content")(
        // Section - Name
        div(cls := "section section--x-centered")(
          if detailsOption.isDefined then
            div(cls := "name")(detailsOption.get.name)
          else
            frag()
        ),
        // Section - Title
        div(cls := "section section--x-centered")(
          if detailsOption.isDefined then
            div(cls := "title")(detailsOption.get.title)
          else
            frag()
        ),
        // Section - Contact Info
        div(cls := "section section--x-centered")(
          if detailsOption.isDefined then
            div(cls := "details")(
              List(
                detailsOption.get.phone,
                detailsOption.get.email,
                detailsOption.get.location
              ).map(value => div(cls := "details__item")(value))
            )
          else
            frag()
        ),
        // Section - Header Summary
        div(cls := "section")(
          div(cls := "header")(
            div(cls := "header-divider")(
              div(cls := "header-divider__line")
            ),
            div(cls := "header__text")("Summary"),
            div(cls := "header-divider")(
              div(cls := "header-divider__line")
            ),
          )
        ),
        // Section - Summary
        div(cls := "section")(
          div()(
            if detailsOption.isDefined then detailsOption.get.summary else frag()
          )
        ),
        // Section - Header Skills
        div(cls := "section")(
          div(cls := "header")(
            div(cls := "header-divider")(
              div(cls := "header-divider__line")
            ),
            div(cls := "header__text")("Skills"),
            div(cls := "header-divider")(
              div(cls := "header-divider__line")
            ),
          )
        ),
        // Section - Skills
        div(cls := "section")(
          div(cls := "skills")(
            for skill <- skills yield
              div(cls := "skills__item")(skill.name)
          )
        ),
        // Section - Header Experience
        div(cls := "section")(
          div(cls := "header")(
            div(cls := "header-divider")(
              div(cls := "header-divider__line")
            ),
            div(cls := "header__text")("Experiences"),
            div(cls := "header-divider")(
              div(cls := "header-divider__line")
            ),
          )
        ),
        // Section - Experience
        div(cls := "section")(
          for experience <- experiences yield
            List(
              div(cls := "experience-details")(
                div(cls := "experience-details__title")(experience.title),
                div()(
                  List(
                    experience.organization,
                    experience.location,
                    experience.duration
                  ).mkString("  |  ")
                ),
              ),
              ul(cls := "experience-descriptions")(
                for description <- experience.description.split("\n") yield
                  li(cls := "experience-descriptions__item")(description)
              )
            )
        ),
        // Section - Header Certification
        div(cls := "section")(
          div(cls := "header")(
            div(cls := "header-divider")(
              div(cls := "header-divider__line")
            ),
            div(cls := "header__text")("Certifications"),
            div(cls := "header-divider")(
              div(cls := "header-divider__line")
            ),
          )
        ),
        // Section - Header
        div(cls := "section")(
          for certification <- certifications yield
            List(
              div(cls := "certification")(
                div(cls := "certification__title")(certification.title),
                div()(
                  List(
                    certification.organization,
                    certification.location,
                    certification.year
                  ).mkString("  |  ")
                )
              )
            )
        ),
        // Section - Socials
        div(cls := "section section--x-centered")(
          div(cls := "socials")(
            for social <- socials yield
              div(cls := "socials__item")(social.url)
          )
        ),
      )
    )

  initialize()
