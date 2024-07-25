package net.sailware.resumewizard.resume

import scalatags.Text.all.*
import net.sailware.resumewizard.core.CorePageView
import net.sailware.resumewizard.resume.Step

object ResumePageView:

  def view(step: Step, formContent: Frag) =
    val content = buildContent(step, formContent)
    CorePageView.view(content)

  def viewCustom(step: Step, content: Frag) =
    CorePageView.view(customForm(step, content))

  def customForm(step: Step, content: Frag) =
    // Wizardly
    div(cls := "row")(
      div(cls := "col-md-12 stretch-card")(
        div(cls := "card")(
          div(cls := "card-body")(
            h4(cls := "card-title")("Resume Wizard"),
            div(cls := "wizardly")(
              buildSteps(step),
              content
            )
          )
        )
      )
    )

  private def buildContent(step: Step, formContent: Frag) =
    // Wizardly
    div(cls := "row")(
      div(cls := "col-md-12 stretch-card")(
        div(cls := "card")(
          div(cls := "card-body")(
            h4(cls := "card-title")("Resume Wizard"),
            div(cls := "wizardly")(
              buildSteps(step),
              buildForm(step, formContent),
            )
          )
        )
      )
    )

  private def buildSteps(current: Step) = 
    ul(cls := "wizardly__steps")(
      for step <- Step.values
      yield buildStep(step, step == current)
    )

  private def buildStep(step: Step, current: Boolean) =
    val label = s"${step.ordinal + 1}. ${step.label}"
    val stepText =
      if current then
        span(cls := "wizardly-step__link wizardly-step__link--current")(label)
      else
        a(cls := "wizardly-step__link wizardly-step__link--active", href := s"/wizard/${step.toString().toLowerCase()}")(label)

    li(cls := "wizardly-step")(
      stepText
    )

  private def buildForm(step: Step, formInputs: Frag) =
    form(method := "post", action := s"/wizard/${step.toString().toLowerCase()}")(
      div(cls := "wizardly__content")(
        div(cls := "wizardly-form")(
          formInputs,
        ),
        buildActions(step)
      )
    )

  private def buildActions(current: Step) =
    div(cls := "wizardly-actions")(
      if hasNext(current) then button(cls := "btn btn-primary", `type` := "submit")("Next") else ""
    )

  private def hasNext(step: Step) =
    step.ordinal != Step.values.length - 1
