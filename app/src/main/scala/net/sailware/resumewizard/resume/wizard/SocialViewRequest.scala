package net.sailware.resumewizard.resume.wizard

import net.sailware.resumewizard.resume.Step

case class SocialViewRequest(
  val step: Step,
  val socials: List[(Int, String, String)]
)
