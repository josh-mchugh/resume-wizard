package net.sailware.resumewizard.resume.wizard

import net.sailware.resumewizard.database.DatabaseResource
import net.sailware.resumewizard.jooq.Tables.{
  RESUME_CERTIFICATIONS,
  RESUME_DETAILS,
  RESUME_EXPERIENCES,
  RESUME_SKILLS,
  RESUME_SOCIALS
}
import net.sailware.resumewizard.jooq.tables.records.{
  ResumeCertificationsRecord,
  ResumeDetailsRecord,
  ResumeExperiencesRecord,
  ResumeSkillsRecord,
  ResumeSocialsRecord
}
import net.sailware.resumewizard.resume.ResumePageView
import net.sailware.resumewizard.resume.Step
import scala.jdk.OptionConverters.RichOptional
import scalatags.Text.all.*

case class ResumeReviewRoutes(databaseResource: DatabaseResource)(implicit cc: castor.Context, log: cask.Logger) extends cask.Routes:
  val dslContext = databaseResource.ctx

  @cask.get("/wizard/review")
  def getWizardReview() =
    val detailsOption = dslContext.fetchOptional(RESUME_DETAILS).toScala
    val socialsOption = dslContext.fetchOptional(RESUME_SOCIALS).toScala
    val experiencesOption = dslContext.fetchOptional(RESUME_EXPERIENCES).toScala
    val skillsOption = dslContext.fetchOptional(RESUME_SKILLS).toScala
    val certificationsOption = dslContext.fetchOptional(RESUME_CERTIFICATIONS).toScala
    ResumePageView.buildPage(ResumePageView.buildSteps(Step.Review), buildReview(detailsOption, socialsOption, experiencesOption, skillsOption, certificationsOption))

  def buildReview(detailsOption: Option[ResumeDetailsRecord], socialsOption: Option[ResumeSocialsRecord], experiencesOption: Option[ResumeExperiencesRecord], skillsOption: Option[ResumeSkillsRecord], certificationsOption: Option[ResumeCertificationsRecord]) =
    val details = List(
        p(strong("Name: "), detailsOption.get.getName()),
        p(strong("Title: "), detailsOption.get.getTitle()),
        p(strong("Summary: "), detailsOption.get.getSummary()),
        p(strong("Phone: "), detailsOption.get.getPhone()),
        p(strong("Email: "), detailsOption.get.getEmail()),
        p(strong("Location: "), detailsOption.get.getLocation()),
    )

    val socials = List(
      p(strong("Social Name: "), socialsOption.get.getName()),
      p(strong("Social URL: "), socialsOption.get.getUrl()),
    )

    val experiences = List(
      p(strong("Experience Title: "), experiencesOption.get.getTitle()),
      p(strong("Experience Organization: "), experiencesOption.get.getOrganization()),
      p(strong("Experience Duration: "), experiencesOption.get.getDuration()),
      p(strong("Experience Location: "), experiencesOption.get.getLocation()),
      p(strong("Experience Description: "), experiencesOption.get.getDescription()),
      p(strong("Experience Skills: "), experiencesOption.get.getSkills()),
    )

    val skills = List(
      p(strong("Skill Name: "), skillsOption.get.getName()),
      p(strong("Skill Rating: "), skillsOption.get.getRating().toString()),
    )

    val certifications = List(
      p(strong("Certification Title: ", certificationsOption.get.getTitle())),
      p(strong("Certification Organization: "), certificationsOption.get.getOrganization()),
      p(strong("Certification Year: "), certificationsOption.get.getYear()),
      p(strong("Certification Location: "), certificationsOption.get.getLocation()),
    )

    div()(
      if detailsOption.isDefined then details else frag(),
      if socialsOption.isDefined then socials else frag(),
      if experiencesOption.isDefined then experiences else frag(),
      if skillsOption.isDefined then skills else frag(),
      if certificationsOption.isDefined then certifications else frag()
    )



    


  initialize()
