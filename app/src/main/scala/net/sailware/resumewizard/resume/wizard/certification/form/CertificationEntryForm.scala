package net.sailware.resumewizard.resume.wizard.certification.form

case class CertificationEntryForm(
  val id: Int,
  val title: String,
  val organization: String,
  val year: String,
  val location: String
)
