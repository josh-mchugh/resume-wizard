package net.sailware.resumewizard.resume.wizard.experience.form

case class ExperienceNewEntryForm(
  val title: String,
  val organization: String,
  val duration: String,
  val location: String,
  val description: String,
  val skills: String
)