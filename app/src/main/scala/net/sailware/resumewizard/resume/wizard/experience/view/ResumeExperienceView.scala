package net.sailware.resumewizard.resume.wizard.experience.view

import net.sailware.resumewizard.resume.Experience
import net.sailware.resumewizard.resume.ResumePageView
import net.sailware.resumewizard.resume.wizard.experience.view.model.ExperienceViewRequest
import scalatags.Text.all.*

object ResumeExperienceView:

  def view(request: ExperienceViewRequest) =
    ResumePageView.view(request.step, formContent(request.experiences))

  private def formContent(experiences: List[Experience]) =
    form(cls := "sheet-form", method := "post", action := "/wizard/experience")(
      div(attr("data-controller") := "fieldsets")(
        div(cls := "sheet-form-header")(
          button(cls := "btn btn--outline-primary sheet-form-header__add-btn", `type` := "button", attr("data-action") := "click->fieldsets#addEntry")("Add Experience"),
          h1(cls := "sheet-form-header__title")("Experiences"),
          button(cls := "btn btn--primary sheet-form-header__save-btn", `type` := "submit")("Save")
        ),
        div(cls := "sheet-form__content", attr("data-fieldsets-target") := "container")(
          buildEntries(experiences),
        ),
        template(),
      ),
    )
     
  private def buildEntries(experiences: List[Experience]) =
    if experiences.nonEmpty then
      experiences.map(experience => buildEntry("entry", experience.id.toString(), experience.title, experience.organization, experience.duration, experience.location, experience.description, experience.skills))
    else
      List(buildEntry("newEntry", "0", "", "", "", "", "", ""))

  private def template() =
    tag("template")(attr("data-fieldsets-target") := "template")(
      buildEntry("newEntry", "__ID_REPLACE__", "", "", "", "", "", "")
    )

  private def buildEntry(fieldName: String, id: String, title: String, organization: String, duration: String, location: String, description: String, skills: String) =
    div(cls := "entry", attr("data-fieldsets-target") := fieldName)(
      div(cls := "entry__fields")(
        div(cls := "form-group")(
          label(cls := "form-label")("Title"),
          input(cls := "form-control", `type` := "text", name := s"$fieldName[$id].title", placeholder := "Title", value := title)
        ),
        div(cls := "form-group")(
          label(cls := "form-label")("Organization"),
          input(cls := "form-control", `type` := "text", name := s"$fieldName[$id].organization", placeholder := "Organization", value := organization)
        ),
        div(cls := "form-group")(
          label(cls := "form-label")("Duration"),
          input(cls := "form-control", `type` := "text", name := s"$fieldName[$id].duration",  placeholder := "Duration", value := duration)
        ),
        div(cls := "form-group")(
          label(cls := "form-label")("Location"),
          input(cls := "form-control", `type` := "text", name := s"$fieldName[$id].location", placeholder := "Location", value := location)
        ),
        div(cls := "form-group")(
          label(cls := "form-label")("Description"),
          textarea(cls := "form-control", rows := 3, name := s"$fieldName[$id].description", placeholder := "Description")(description)
        ),
        div(cls := "form-group")(
          label(cls := "form-label")("Skills"),
          textarea(cls := "form-control", rows := 3, name := s"$fieldName[$id].skills",  placeholder := "Skills")(skills)
        )
      ),
      div(cls := "entry__delete")(
        button(cls := "btn btn--entry-remove", `type` := "button", attr("data-action") := "click->fieldsets#removeEntry")("X")
      ),
    )
