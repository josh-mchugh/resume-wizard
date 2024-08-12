package net.sailware.resumewizard.resume

import scalatags.Text.all.*
import net.sailware.resumewizard.core.CorePageView
import net.sailware.resumewizard.resume.Step

object ResumePageView:

  def view(step: Step, content: Frag) =
    CorePageView.view(topNavItems(step), content)

  private def topNavItems(current: Step) =
    Step.values.map { step =>
      if step == current then
        span(cls := "top-nav__item top-nav__item--current")(step.label)
      else
        a(cls := "top-nav__item", href := s"/wizard/${step.toString().toLowerCase()}")(step.label)
    }
      .toList
      

    
