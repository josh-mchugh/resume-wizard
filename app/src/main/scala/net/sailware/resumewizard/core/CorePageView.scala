package net.sailware.resumewizard.core

import scalatags.Text.all.*
import scalatags.Text.tags2.nav
import scalatags.Text.tags2.section
import scalatags.Text.tags2.title

object CorePageView:

  def view(content: Frag) =
    doctype("html")(
      html(
        title("Resume Wizard"),
        head(
          link(rel := "stylesheet", href := "/static/style.min.css"),
          link(rel := "stylesheet", href := "/static/styles.css"),
          script(src := " https://cdn.jsdelivr.net/npm/@hotwired/turbo@8.0.5/dist/turbo.es2017-umd.min.js"),
          script(`type` := "module")(
            raw("""
                  import { Application, Controller } from "https://unpkg.com/@hotwired/stimulus/dist/stimulus.js"
                  window.Stimulus = Application.start()

                  Stimulus.register("fieldsets", class extends Controller {
                    static targets = [ "container", "template", "entry", "newEntry" ]
                    connect() { }
                    addEntry() {
                      this.containerTarget.insertAdjacentHTML("beforeend", this.templateTarget.innerHTML.replaceAll("__ID_REPLACE__", this.newEntryTargets.length))
                    }
                    removeEntry(event) {
                      event.target.parentElement.parentElement.remove()
                      this.newEntryTargets.forEach(function(value, index){
                        value.innerHTML = value.innerHTML.replaceAll(/newEntry\[\d+\]/g, `newEntry[${index}]`)
                      })
                    }
                  })
                """)
          )
        ),
        body(
          div(cls := "main-wrapper")(
            // top navigation
            nav(cls := "horizontal-menu")(
              div(cls := "navbar top-navbar")(
                div(cls := "container")(
                  div(cls := "navbar-content")(
                    a(cls :="navbar-brand", href := "#")("Resume", span("Wizard"))
                  )
                )
              )
            ),
            // Page Content
            div(cls := "page-wrapper")(
              div(cls := "page-content")(
                content
              ),
              // Footer
              footer(cls := "footer border-top")(
                div(cls := "container d-flex flex-column flex-md-row align-items-center justify-content-between py-3 small")(
                  p(cls := "text-muted mb-1 mb-md-0")("Copyright 2024"),
                  p(cls := "text-muted")("Maded For Fun")
                )
              )
            )
          )
        )
      )
    )
