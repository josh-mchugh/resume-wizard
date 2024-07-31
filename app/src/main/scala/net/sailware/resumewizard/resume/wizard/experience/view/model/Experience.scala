package net.sailware.resumewizard.resume.wizard.experience.view.model

import net.sailware.resumewizard.jooq.tables.records.ResumeExperiencesRecord

case class Experience(
  val id: Int,
  val title: String,
  val organization: String,
  val duration: String,
  val location: String,
  val description: String,
  val skills: String
)

object Experience:

  def apply(record: ResumeExperiencesRecord): Experience =
    Experience(
      record.getId(),
      record.getTitle(),
      record.getOrganization(),
      record.getDuration(),
      record.getLocation(),
      record.getDescription(),
      record.getSkills()
    )
