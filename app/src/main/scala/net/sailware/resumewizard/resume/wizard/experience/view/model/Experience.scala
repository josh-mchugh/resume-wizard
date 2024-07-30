package net.sailware.resumewizard.resume.wizard.experience.view.model

case class Experience(
  val id: Int,
  val title: String,
  val organization: String,
  val duration: String,
  val location: String,
  val description: String,
  val skills: String
)
