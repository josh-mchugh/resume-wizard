package net.sailware.resumewizard.resume.wizard.experience.form

case class ExperienceUpdateForm(
  val id: Int,
  val title: String,
  val organization: String,
  val duration: String,
  val location: String,
  val description: String,
  val skills: String
)
