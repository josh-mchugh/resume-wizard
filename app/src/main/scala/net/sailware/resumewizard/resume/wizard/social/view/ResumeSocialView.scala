package net.sailware.resumewizard.resume.wizard.social.view

import net.sailware.resumewizard.resume.Social
import net.sailware.resumewizard.resume.ResumePageView
import net.sailware.resumewizard.resume.wizard.social.view.model.SocialViewRequest
import scalatags.Text.all.*

object ResumeSocialView:

  def view(request: SocialViewRequest) =
    ResumePageView.view(request.step, formContent(request.socials))

  private def formContent(socials: List[Social]) =
    form(cls := "sheet-form", method := "post", action := "/wizard/social")(
      div(attr("data-controller") := "fieldsets")(
        div(cls := "sheet-form-header")(
          button(cls := "btn btn--outline-primary sheet-form-header__add-btn", `type` := "button", attr("data-action") := "click->fieldsets#addEntry")("Add Social"),
          h1(cls := "sheet-form-header__title")("Socials"),
          button(cls := "btn btn--primary sheet-form-header__save-btn", `type` := "submit")("Save")
        ),
        div(cls := "sheet-form__content", attr("data-fieldsets-target") := "container")(
          buildEntries(socials),
        ),
        template()
      ),
    )
     
  private def buildEntries(socials: List[Social]) =
    if socials.nonEmpty then
      socials.map(social => buildEntry("entry", social.id.toString(), social.name, social.url))
    else
      List(buildEntry("newEntry", "0", "", ""))

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
        div(cls := "form-group")(
          label(cls := "form-label")("Name"),
          input(cls := "form-control", `type` := "text", name := s"$fieldName[$id].name", placeholder := "Name", value := socialName)
        ),
        div(cls := "form-group")(
          label(cls := "form-label")("URL"),
          input(cls := "form-control", `type` := "text", name := s"$fieldName[$id].url", placeholder := "URL", value := url)
        ),
      )
    )
