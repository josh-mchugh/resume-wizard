package net.sailware.resumewizard.resume.wizard.detail.view

import net.sailware.resumewizard.resume.Detail
import net.sailware.resumewizard.resume.ResumePageView
import net.sailware.resumewizard.resume.wizard.detail.view.model.DetailViewRequest
import scalatags.Text.all.*

object ResumeDetailView:

  def view(request: DetailViewRequest) =
    ResumePageView.viewCustom(request.step, formContent(request.detail))

  private def formContent(detail: Detail) =
    div(attr("data-controller") := "fieldsets")(
      form(id := "form", method := "post", action := "/wizard/detail")(
        div(cls := "wizardly__content")( 
          div(cls := "wizardly-form", attr("data-fieldsets-target") := "container")(
            buildEntry(detail),
          ),
          div(cls := "wizardly-actions")(
            button(cls := "btn btn-primary", `type` := "submit")("Next")
          )
        )
      ),
    )

  def buildEntry(detail: Detail) =
    List(
      div()(
        label(cls := "form-label")("Name"),
        input(cls := "form-control", `type` := "text", name := "name", placeholder := "Name", value := detail.name)
      ),
      div(cls := "mt-3")(
        label(cls := "form-label")("Title"),
        input(cls := "form-control", `type` := "text", name := "title", placeholder := "Title", value := detail.title)
      ),
      div(cls := "mt-3")(
        label(cls := "form-label")("Summary"),
        textarea(cls := "form-control", rows := 3, name := "summary",  placeholder := "Summary of your current or previous role")(detail.summary)
      ),
      div()(
        label(cls := "form-label")("Phone Number"),
        input(cls := "form-control", `type` := "text", name := "phone", placeholder := "Phone Number", value := detail.phone)
      ),
      div(cls := "mt-3")(
        label(cls := "form-label")("Email Address"),
        input(cls := "form-control", `type` := "text", name := "email", placeholder := "Email Address", value := detail.email)
      ),
      div(cls := "mt-3")(
        label(cls := "form-label")("Location"),
        input(cls := "form-control", `type` := "text", name := "location", placeholder := "Location", value := detail.location)
      )
    )
