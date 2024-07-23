package net.sailware.resumewizard.resume.wizard

import net.sailware.resumewizard.resume.ResumePageView
import net.sailware.resumewizard.resume.wizard.SocialViewRequest
import scalatags.Text.all.*

object ResumeSocialView:

  def view(request: SocialViewRequest) =
    ResumePageView.view(request.step, form(request.name, request.url))

  private def form(socialName: String, url: String) =
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
