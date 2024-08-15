package net.sailware.resumewizard.resume.wizard.detail.view

import net.sailware.resumewizard.resume.Detail
import net.sailware.resumewizard.resume.ResumePageView
import net.sailware.resumewizard.resume.wizard.detail.view.model.DetailViewRequest
import scalatags.Text.all.*

object ResumeDetailView:

  def view(request: DetailViewRequest) =
    ResumePageView.view(request.step, formContent(request.detail))

  private def formContent(detail: Detail) =
    form(cls := "sheet-form", method := "post", action := "/wizard/detail")(
      div(attr("data-controller") := "fieldsets")(
        div(cls := "sheet-form-header")(
          h1(cls := "sheet-form-header__title")("Details"),
          button(cls := "btn btn--primary sheet-form-header__save-btn", `type` := "submit")("Save")
        ),
        div(cls := "sheet-form__content", attr("data-fieldsets-target") := "container")(
          buildEntry(detail),
        )
      ),
    )

  def buildEntry(detail: Detail) =
    List(
      div(cls := "form-group")(
        label(cls := "form-label")("Name"),
        input(cls := "form-control", `type` := "text", name := "name", placeholder := "Name", value := detail.name)
      ),
      div(cls := "form-group")(
        label(cls := "form-label")("Title"),
        input(cls := "form-control", `type` := "text", name := "title", placeholder := "Title", value := detail.title)
      ),
      div(cls := "form-group")(
        label(cls := "form-label")("Summary"),
        textarea(cls := "form-control", rows := 6, name := "summary",  placeholder := "Summary of your current or previous role")(detail.summary)
      ),
      div(cls := "form-group")(
        label(cls := "form-label")("Phone Number"),
        input(cls := "form-control", `type` := "text", name := "phone", placeholder := "Phone Number", value := detail.phone)
      ),
      div(cls := "form-group")(
        label(cls := "form-label")("Email Address"),
        input(cls := "form-control", `type` := "text", name := "email", placeholder := "Email Address", value := detail.email)
      ),
      div(cls := "form-group")(
        label(cls := "form-label")("Location"),
        input(cls := "form-control", `type` := "text", name := "location", placeholder := "Location", value := detail.location)
      )
    )
