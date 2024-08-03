package net.sailware.resumewizard.resume.wizard.detail.view.model

import net.sailware.resumewizard.resume.Detail
import net.sailware.resumewizard.resume.Step

case class DetailViewRequest(
  val step: Step,
  val detail: Detail
)
