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
            buildEntries(socialName, url),
          ),
          div(cls := "wizardly-actions")(
            button(cls := "btn btn-outline-secondary", `type` := "button", attr("data-action") := "click->fieldsets#addEntry")("Add Another"),
            button(cls := "btn btn-primary", `type` := "submit")("Next")
          )
        )
      ),
      template()
    )
     
  private def buildEntries(socialName: String, url: String) =
    List(buildEntry("entry", "0", socialName, url))

  private def template() =
    tag("template")(attr("data-fieldsets-target") := "template")(
      buildEntry("newEntry", "__ID_REPLACE__", "", "")
    )

  private def buildEntry(fieldName: String, id: String, socialName: String, url: String) =
    div(cls := "wizardly-form-entry", attr("data-fieldsets-target") := fieldName)(
      div(cls := "wizardly-form-entry__delete")(
        button(cls := "btn btn-outline-secondary btn-icon", `type` := "button", attr("data-action") := "click->fieldsets#removeEntry")("X")
      ),
      div(cls := "wizardly-form-entry__form-group")(
        div()(
          label(cls := "form-label")("Name"),
          input(cls := "form-control", `type` := "text", name := s"$fieldName[$id].name", placeholder := "Name", value := socialName)
        ),
        div(cls := "mt-3")(
          label(cls := "form-label")("URL"),
          input(cls := "form-control", `type` := "text", name := s"$fieldName[$id].url", placeholder := "URL", value := url)
        ),
      )
    )
