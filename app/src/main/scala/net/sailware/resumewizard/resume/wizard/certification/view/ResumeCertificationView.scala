package net.sailware.resumewizard.resume.wizard.certification.view

import net.sailware.resumewizard.resume.Certification
import net.sailware.resumewizard.resume.ResumePageView
import net.sailware.resumewizard.resume.wizard.certification.view.model.CertificationViewRequest
import scalatags.Text.all.*

object ResumeCertificationView:

  def view(request: CertificationViewRequest) =
    ResumePageView.view(request.step, formContent(request.certifications))

  private def formContent(certifications: List[Certification]) =
    form(cls := "sheet-form", method := "post", action := "/wizard/certification")(
      div(attr("data-controller") := "fieldsets")(
        div(cls := "sheet-form-header")(
          button(cls := "btn btn--outline-primary sheet-form-header__add-btn", `type` := "button", attr("data-action") := "click->fieldsets#addEntry")("Add Certification"),
          h1(cls := "sheet-form-header__title")("Certifications"),
          button(cls := "btn btn--primary sheet-form-header__save-btn", `type` := "submit")("Save")
        ),
        div(cls := "sheet-form__content", attr("data-fieldsets-target") := "container")(
          buildEntries(certifications),
        ),
        template()
      ),
    )
     
  private def buildEntries(certifications: List[Certification]) =
    if certifications.nonEmpty then
      certifications.map(certification => buildEntry("entry", certification.id.toString, certification.title, certification.organization, certification.year, certification.location))
    else
      List(buildEntry("newEntry", "0", "", "", "", ""))

  private def template() =
    tag("template")(attr("data-fieldsets-target") := "template")(
      buildEntry("newEntry", "__ID_REPLACE__", "", "", "", "")
    )

  private def buildEntry(fieldName: String, id: String, title: String, organization: String, year: String, location: String) =
    div(cls := "wizardly-form-entry", attr("data-fieldsets-target") := fieldName)(
      div(cls := "wizardly-form-entry__delete")(
        button(cls := "btn btn-outline-secondary btn-icon", `type` := "button", attr("data-action") := "click->fieldsets#removeEntry")("X")
      ),
      div(cls := "wizardly-form-entry__form-group")(
        div(cls := "form-group")(
          label(cls := "form-label")("Title"),
          input(cls := "form-control", `type` := "text", name := s"$fieldName[$id].title", placeholder := "Title", value := title)
        ),
        div(cls := "form-group")(
          label(cls := "form-label")("Organization"),
          input(cls := "form-control", `type` := "text", name := s"$fieldName[$id].organization", placeholder := "Organization", value := organization)
        ),
        div(cls := "form-group")(
          label(cls := "form-label")("Year"),
          input(cls := "form-control", `type` := "text", name := s"$fieldName[$id].year", placeholder := "Year", value := year)
        ),
        div(cls := "form-group")(
          label(cls := "form-label")("Location"),
          input(cls := "form-control", `type` := "text", name := s"$fieldName[$id].location", placeholder := "Location", value := location)
        )
      )
    )
