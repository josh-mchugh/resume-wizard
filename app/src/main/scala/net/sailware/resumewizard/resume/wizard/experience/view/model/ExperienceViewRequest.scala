package net.sailware.resumewizard.resume.wizard.experience.view.model

import net.sailware.resumewizard.resume.Experience
import net.sailware.resumewizard.resume.Step

case class ExperienceViewRequest(
  step: Step,
  experiences: List[Experience]
)
