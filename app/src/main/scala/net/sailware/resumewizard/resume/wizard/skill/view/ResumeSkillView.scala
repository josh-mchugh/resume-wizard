package net.sailware.resumewizard.resume.wizard.skill.view

import net.sailware.resumewizard.resume.ResumePageView
import net.sailware.resumewizard.resume.Skill
import net.sailware.resumewizard.resume.wizard.skill.view.model.SkillViewRequest
import scalatags.Text.all.*

object ResumeSkillView:

  def view(request: SkillViewRequest) =
    ResumePageView.view(request.step, formContent(request.skills))

  private def formContent(skills: List[Skill]) =
    form(cls := "sheet-form", method := "post", action := "/wizard/skill")(
      div(attr("data-controller") := "fieldsets")(
        div(cls := "sheet-form-header")(
          button(cls := "btn btn--outline-primary sheet-form-header__add-btn", `type` := "button", attr("data-action") := "click->fieldsets#addEntry")("Add Skill"),
          h1(cls := "sheet-form-header__title")("Skills"),
          button(cls := "btn btn--primary sheet-form-header__save-btn", `type` := "submit")("Save")
        ),
        div(cls := "sheet-form__content", attr("data-fieldsets-target") := "container")(
          buildEntries(skills),
        ),
        template()
      ),
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
    div(cls := "entry", attr("data-fieldsets-target") := fieldName)(
      div(cls := "entry__fields")(
        div(cls := "form-group")(
          label(cls := "form-label")("Name"),
          input(cls := "form-control", `type` := "text", name := s"$fieldName[$id].name", placeholder := "Name", value := skillName)
        ),
        div(cls := "form-group")(
          label(cls := "form-label")("Rating"),
        input(cls := "form-range", `type` := "range", name := s"$fieldName[$id].rating", min := 0, max := 5, step := 1, value := rating)
        ),
      ),
      div(cls := "entry__delete")(
        button(cls := "btn btn--entry-remove", `type` := "button", attr("data-action") := "click->fieldsets#removeEntry")("X")
      ),
    )
