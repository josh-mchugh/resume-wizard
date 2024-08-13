package net.sailware.resumewizard.core

import scalatags.Text.all.*
import scalatags.Text.tags2.nav
import scalatags.Text.tags2.section
import scalatags.Text.tags2.title

object CorePageView:

  def view(topNavItems: List[Frag], content: Frag) =
    doctype("html")(
      html(
        title("Resume Wizard"),
        head(
          link(rel := "preconnect", href := "https://fonts.googleapis.com"),
          link(rel := "preconnect", href := "https://fonts.gstatic.com"),
          link(rel := "stylesheet", href := "https://fonts.googleapis.com/css2?family=Roboto:ital,wght@0,100;0,300;0,400;0,500;0,700;0,900;1,100;1,300;1,400;1,500;1,700;1,900&family=Source+Code+Pro:ital,wght@0,200..900;1,200..900&display=swap"),
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
          // top navigation
          nav(cls := "top-nav")(
            a(cls := "top-nav__logo", href := "/wizard/detail")(
              "Resume",
              span(cls := "top-nav__logo--light")("Wizard")
            ),
            topNavItems,
          ),
          // Page Content
          div(cls := "content")(
            div(cls := "sheet")(
              content
            ),
          ),
          // Footer
          footer(cls := "footer")(
            span(cls := "footer__text")("Maded For Fun"),
            span(cls := "footer__text")("Copyright 2024")
          )
        )
      )
    )

