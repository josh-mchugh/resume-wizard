package net.sailware.resumewizard.resume.wizard.certification.view.model

import net.sailware.resumewizard.resume.Certification
import net.sailware.resumewizard.resume.Step

case class CertificationViewRequest(
  val step: Step,
  val certifications: List[Certification]
)
