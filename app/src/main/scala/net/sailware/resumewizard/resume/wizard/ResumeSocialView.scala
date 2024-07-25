package net.sailware.resumewizard.resume.wizard

import net.sailware.resumewizard.resume.ResumePageView
import net.sailware.resumewizard.resume.wizard.SocialViewRequest
import scalatags.Text.all.*

object ResumeSocialView:

  def view(request: SocialViewRequest) =
    ResumePageView.viewCustom(request.step, formContent(request.name, request.url))

  private def formContent(socialName: String, url: String) =
    form(id := "form", method := "post", action := "/wizard/social")(
      div(cls := "wizardly__content")(
        div(cls := "wizardly-form")(
          inputFields(socialName, url),
        ),
        button(cls := "btn btn-primary", `type` := "submit")("Next")
      ),
      div(attr("data-controller") := "hello")(
        input(attr("data-hello-target") := "name")
      )
    )
     
  private def inputFields(socialName: String, url: String) =
    List(
      div()(
        label(cls := "form-label")("Name"),
        input(cls := "form-control", `type` := "text", name := "form[0].name", placeholder := "Name", value := socialName)
      ),
      div(cls := "mt-3")(
        label(cls := "form-label")("URL"),
        input(cls := "form-control", `type` := "text", name := "form[0].url", placeholder := "URL", value := url)
      ),
    )
