package net.sailware.resumewizard.resume.wizard.skill.view

import net.sailware.resumewizard.resume.ResumePageView
import net.sailware.resumewizard.resume.wizard.skill.view.model.Skill
import net.sailware.resumewizard.resume.wizard.skill.view.model.SkillViewRequest
import scalatags.Text.all.*

object ResumeSkillView:

  def view(request: SkillViewRequest) =
    ResumePageView.viewCustom(request.step, formContent(request.skills))

  private def formContent(skills: List[Skill]) =
    div(attr("data-controller") := "fieldsets")(
      form(id := "form", method := "post", action := "/wizard/skill")(
        div(cls := "wizardly__content")( 
          div(cls := "wizardly-form", attr("data-fieldsets-target") := "container")(
            buildEntries(skills),
          ),
          div(cls := "wizardly-actions")(
            button(cls := "btn btn-outline-secondary", `type` := "button", attr("data-action") := "click->fieldsets#addEntry")("Add Another"),
            button(cls := "btn btn-primary", `type` := "submit")("Next")
          )
        )
      ),
      template()
    )
     
  private def buildEntries(skills: List[Skill]) =
    if skills.nonEmpty then
      skills.map(skill => buildEntry("entry", skill.id.toString(), skill.name, skill.rating))
    else
      List(buildEntry("newEntry", "0", "", 0))

  private def template() =
    tag("template")(attr("data-fieldsets-target") := "template")(
      buildEntry("newEntry", "__ID_REPLACE__", "", 0)
    )

  private def buildEntry(fieldName: String, id: String, skillName: String, rating: Short) =
    div(cls := "wizardly-form-entry", attr("data-fieldsets-target") := fieldName)(
      div(cls := "wizardly-form-entry__delete")(
        button(cls := "btn btn-outline-secondary btn-icon", `type` := "button", attr("data-action") := "click->fieldsets#removeEntry")("X")
      ),
      div(cls := "wizardly-form-entry__form-group")(
        div()(
          label(cls := "form-label")("Name"),
          input(cls := "form-control", `type` := "text", name := s"$fieldName[$id].name", placeholder := "Name", value := skillName)
        ),
        div(cls := "mt-3")(
          label(cls := "form-label")("Rating"),
        input(cls := "form-range", `type` := "range", name := s"$fieldName[$id].rating", min := 0, max := 5, step := 1, value := rating)
        ),
      )
    )
