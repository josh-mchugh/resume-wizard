package net.sailware.resumewizard.resume.wizard.social.view.model

import net.sailware.resumewizard.resume.Step

case class SocialViewRequest(
  val step: Step,
  val socials: List[Social]
)
