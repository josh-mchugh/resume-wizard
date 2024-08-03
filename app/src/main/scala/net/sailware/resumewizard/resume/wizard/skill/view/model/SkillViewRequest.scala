package net.sailware.resumewizard.resume.wizard.skill.view.model

import net.sailware.resumewizard.resume.Skill
import net.sailware.resumewizard.resume.Step

case class SkillViewRequest(
  val step: Step,
  val skills: List[Skill]
)
