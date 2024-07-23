package net.sailware.resumewizard.resume.wizard

import net.sailware.resumewizard.resume.Step

case class SocialViewRequest(
  val step: Step,
  val name: String,
  val url: String
)
