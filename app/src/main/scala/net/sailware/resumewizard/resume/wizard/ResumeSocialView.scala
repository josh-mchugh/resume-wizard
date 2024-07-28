package net.sailware.resumewizard.resume.wizard

import net.sailware.resumewizard.resume.ResumePageView
import net.sailware.resumewizard.resume.wizard.SocialViewRequest
import scalatags.Text.all.*

object ResumeSocialView:

  def view(request: SocialViewRequest) =
    ResumePageView.viewCustom(request.step, formContent(request.name, request.url))

  private def formContent(socialName: String, url: String) =
    div(attr("data-controller") := "fieldsets")(
      form(id := "form", method := "post", action := "/wizard/social")(
        div(cls := "wizardly__content")( 
          div(cls := "wizardly-form", attr("data-fieldsets-target") := "container")(
            inputFields(socialName, url),
          ),
          div(cls := "wizardly-actions")(
            button(cls := "btn btn-outline-secondary", `type` := "button", attr("data-action") := "click->fieldsets#addEntry")("Add Another"),
            button(cls := "btn btn-primary", `type` := "submit")("Next")
          )
        )
      ),
      template()
    )
     
  private def inputFields(socialName: String, url: String) =
    List(
      div(cls := "wizardly-form-entry", attr("data-fieldset-target") := "entry")(
        div(cls := "wizardly-form-entry__delete")(
          button(cls := "btn btn-outline-secondary btn-icon", `type` := "button", attr("data-action") := "click->fieldsets#removeEntry")("X")
        ),
        div(cls := "wizardly-form-entry__form-group")(
          div()(
            label(cls := "form-label")("Name"),
            input(cls := "form-control", `type` := "text", name := "entry[0].name", placeholder := "Name", value := socialName)
          ),
          div(cls := "mt-3")(
            label(cls := "form-label")("URL"),
            input(cls := "form-control", `type` := "text", name := "entry[0].url", placeholder := "URL", value := url)
          ),
        )
      )
    )

  private def template() =
    tag("template")(attr("data-fieldsets-target") := "template")(
      div(cls := "wizardly-form-entry", attr("data-fieldsets-target") := "newEntry")(
        div(cls := "wizardly-form-entry__delete")(
          button(cls := "btn btn-outline-secondary btn-icon", `type` := "button", attr("data-action") := "click->fieldsets#removeEntry")("X")
        ),
        div(cls := "wizardly-form-entry__form-group")(
          div()(
            label(cls := "form-label")("Name"),
            input(cls := "form-control", `type` := "text", name := "newEntry[0].name", placeholder := "Name")
          ),
          div(cls := "mt-3")(
            label(cls := "form-label")("URL"),
            input(cls := "form-control", `type` := "text", name := "newEntry[0].url", placeholder := "URL")
          ),
        )
      )
    )
