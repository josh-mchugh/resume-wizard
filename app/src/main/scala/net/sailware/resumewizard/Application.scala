package net.sailware.resumewizard

import scalatags.Text.all.*
import scalatags.Text.tags2.nav
import scalatags.Text.tags2.section
import scalatags.Text.tags2.title
import java.sql.{Connection, DriverManager}
import org.flywaydb.core.Flyway
import org.jooq.DSLContext
import org.jooq.Result
import org.jooq.Record
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import org.jooq.impl.DSL.field
import org.jooq.impl.DSL.table
import net.sailware.resumewizard.jooq.Tables.*

case class StaticRoutes()(implicit cc: castor.Context, log: cask.Logger) extends cask.Routes:

  @cask.staticFiles("/static/")
  def staticFilesRoutes() = "app/src/main/resources/public"

  initialize()

case class RootRoutes()(implicit cc: castor.Context, log: cask.Logger) extends cask.Routes:

  enum Step(val label: String):
    case Name extends Step("Name & Title")
    case Contact extends Step("Contacts")
    case Social extends Step("Socials")
    case Experience extends Step("Experiences")
    case Skill extends Step("Skills")
    case Certification extends Step("Certifications")
    case Review extends Step("Review")

  case class Social(val name: String, val url: String)
  case class Experience(
    val title: String,
    val organization: String,
    val duration: String,
    val location: String,
    val description: String,
    val skills: String
  )
  case class Skill(val name: String, val rating: Int)
  case class Certification(val title: String, val organization: String, val year: String, val location: String)
  case class Resume(
    val name: String,
    val title: String,
    val summary: String,
    val phone: String,
    val email: String,
    val location: String,
    val social: Social,
    val experience: Experience,
    val skill: Skill,
    val certification: Certification
  )
  var resume = Resume(
    "", "", "", "", "", "",
    Social("", ""),
    Experience("", "", "", "", "", ""),
    Skill("", 0),
    Certification("", "", "", "")
  )

  @cask.get("/wizard/name")
  def getWizardName() =
    buildPage(Step.Name)

  @cask.postForm("/wizard/name")
  def postWizardName(name: String, title: String, summary: String) =
    resume = resume.copy(name = name, title = title, summary = summary)
    cask.Redirect("/wizard/contact")

  @cask.get("/wizard/contact")
  def getWizardContact() =
    buildPage(Step.Contact)

  @cask.postForm("/wizard/contact")
  def postWizardContact(phone: String, email: String, location: String) =
    resume = resume.copy(phone = phone, email = email, location = location)
    cask.Redirect("/wizard/social")

  @cask.get("/wizard/social")
  def getWizardSocial() =
    buildPage(Step.Social)

  @cask.postForm("/wizard/social")
  def postWizardSocial(name: String, url: String) =
    resume = resume.copy(social = Social(name, url))
    cask.Redirect("/wizard/experience")

  @cask.get("/wizard/experience")
  def getWizardExperience() =
    buildPage(Step.Experience)

  @cask.postForm("/wizard/experience")
  def postWizardExperience(title: String, organization: String, duration: String, location: String, description: String, skills: String) =
    resume = resume.copy(experience = Experience(title = title, organization = organization, duration = duration, location = location, description = description, skills = skills))
    cask.Redirect("/wizard/skill")

  @cask.get("/wizard/skill")
  def getWizardSkill() =
    buildPage(Step.Skill)

  @cask.postForm("/wizard/skill")
  def postWizardSkill(name: String, rating: Int) =
    resume = resume.copy(skill = Skill(name, rating))
    cask.Redirect("/wizard/certification")

  @cask.get("/wizard/certification")
  def getWizardCertification() =
    buildPage(Step.Certification)

  @cask.postForm("/wizard/certification")
  def postWizardCertification(title: String, organization: String, year: String, location: String) =
    resume = resume.copy(certification = Certification(title, organization, year, location))
    cask.Redirect("/wizard/review")

  @cask.get("/wizard/review")
  def getWizardReview() =
    buildPage(Step.Review)

  def buildPage(step: Step) =
    doctype("html")(
      html(
        title("Resume Wizard"),
        head(
          link(rel := "stylesheet", href := "/static/style.min.css"),
          link(rel := "stylesheet", href := "/static/styles.css")
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
                // Wizardly
                div(cls := "row")(
                  div(cls := "col-md-12 stretch-card")(
                    div(cls := "card")(
                      div(cls := "card-body")(
                        h4(cls := "card-title")("Resume Wizard"),
                        div(cls := "wizardly")(
                          buildSteps(step),
                          buildContent(step),
                        )
                      )
                    )
                  )
                )
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

  def buildSteps(current: Step) = 
    ul(cls := "wizardly__steps")(
      for step <- Step.values
      yield buildStep(step, step == current)
    )

  def buildStep(step: Step, current: Boolean) =
    val label = s"${step.ordinal + 1}. ${step.label}"
    val stepText =
      if current then
        span(cls := "wizardly-step__link wizardly-step__link--current")(label)
      else
        a(cls := "wizardly-step__link wizardly-step__link--active", href := s"/wizard/${step.toString().toLowerCase()}")(label)

    li(cls := "wizardly-step")(
      stepText
    )

  def buildContent(step: Step) =
    step match
      case Step.Name => buildForm(step, buildNameAndTitleForm())
      case Step.Contact => buildForm(step, buildContactsForm())
      case Step.Social => buildForm(step, buildSocialsForm())
      case Step.Experience => buildForm(step, buildExperienceForm())
      case Step.Skill => buildForm(step, buildSkillForm())
      case Step.Certification => buildForm(step, buildCertificationForm())
      case Step.Review => buildReview(step)

  def buildForm(step: Step, formInputs: Frag) =
    form(method := "post", action := s"/wizard/${step.toString().toLowerCase()}")(
      div(cls := "wizardly__content")(
        div(cls := "wizardly-form")(
          formInputs,
        ),
        buildActions(step)
      )
    )

  def buildNameAndTitleForm() =
    List(
      div()(
        label(cls := "form-label")("Name"),
        input(cls := "form-control", `type` := "text", name := "name", placeholder := "Name")
      ),
      div(cls := "mt-3")(
        label(cls := "form-label")("Title"),
        input(cls := "form-control", `type` := "text", name := "title", placeholder := "Title")
      ),
      div(cls := "mt-3")(
        label(cls := "form-label")("Summary"),
        textarea(cls := "form-control", rows := 3, name := "summary",  placeholder := "Summary of your current or previous role")
      )
    )

  def buildContactsForm() =
    List(
      div()(
        label(cls := "form-label")("Phone Number"),
        input(cls := "form-control", `type` := "text", name := "phone", placeholder := "Phone Number")
      ),
      div(cls := "mt-3")(
        label(cls := "form-label")("Email Address"),
        input(cls := "form-control", `type` := "text", name := "email", placeholder := "Email Address")
      ),
      div(cls := "mt-3")(
        label(cls := "form-label")("Location"),
        input(cls := "form-control", `type` := "text", name := "location", placeholder := "Location")
      )
    )

  def buildSocialsForm() =
    List(
      div()(
        label(cls := "form-label")("Name"),
        input(cls := "form-control", `type` := "text", name := "name", placeholder := "Name")
      ),
      div(cls := "mt-3")(
        label(cls := "form-label")("URL"),
        input(cls := "form-control", `type` := "text", name := "url", placeholder := "URL")
      )
    )

  def buildExperienceForm() =
    List(
      div()(
        label(cls := "form-label")("Title"),
        input(cls := "form-control", `type` := "text", name := "title", placeholder := "Title")
      ),
      div(cls := "mt-3")(
        label(cls := "form-label")("Organization"),
        input(cls := "form-control", `type` := "text", name := "organization", placeholder := "Organization")
      ),
      div(cls := "mt-3")(
        label(cls := "form-label")("Duration"),
        input(cls := "form-control", `type` := "text", name := "duration",  placeholder := "Duration")
      ),
      div(cls := "mt-3")(
        label(cls := "form-label")("Location"),
        input(cls := "form-control", `type` := "text", name := "location", placeholder := "Location")
      ),
      div(cls := "mt-3")(
        label(cls := "form-label")("Description"),
        textarea(cls := "form-control", rows := 3, name := "description", placeholder := "Description")
      ),
      div(cls := "mt-3")(
        label(cls := "form-label")("Skills"),
        textarea(cls := "form-control", rows := 3, name := "skills",  placeholder := "Skills")
      )
    )

  def buildSkillForm() =
    List(
      div()(
        label(cls := "form-label")("Name"),
        input(cls := "form-control", `type` := "text", name := "name", placeholder := "Name")
      ),
      div(cls := "mt-3")(
        label(cls := "form-label")("Rating"),
        input(cls := "form-range", `type` := "range", name := "rating", min := 0, max := 5, step := 1)
      )
    )

  def buildCertificationForm() =
    List(
      div()(
        label(cls := "form-label")("Title"),
        input(cls := "form-control", `type` := "text", name := "title", placeholder := "Title")
      ),
      div(cls := "mt-3")(
        label(cls := "form-label")("Organization"),
        input(cls := "form-control", `type` := "text", name := "organization", placeholder := "Organization")
      ),
      div(cls := "mt-3")(
        label(cls := "form-label")("Year"),
        input(cls := "form-control", `type` := "text", name := "year", placeholder := "Year")
      ),
      div(cls := "mt-3")(
        label(cls := "form-label")("Location"),
        input(cls := "form-control", `type` := "text", name := "location", placeholder := "Location")
      )
    )

  def buildActions(current: Step) =
    div(cls := "wizardly-actions")(
      if hasNext(current) then button(cls := "btn btn-primary", `type` := "submit")("Next") else ""
    )

  def hasNext(step: Step) =
    step.ordinal != Step.values.length - 1

  def buildReview(step: Step) =
    div()(
      div(id := "main")(
        p(strong("Name: "), resume.name),
        p(strong("Title: "), resume.title),
        p(strong("Summary: "), resume.summary),
        p(strong("Phone: "), resume.phone),
        p(strong("Email: "), resume.email),
        p(strong("Location: "), resume.location),
        p(strong("Social Name: "), resume.social.name),
        p(strong("Social URL: "), resume.social.url),
        p(strong("Experience Title: "), resume.experience.title),
        p(strong("Experience Organization: "), resume.experience.organization),
        p(strong("Experience Duration: "), resume.experience.duration),
        p(strong("Experience Location: "), resume.experience.location),
        p(strong("Experience Description: "), resume.experience.description),
        p(strong("Experience Skills: "), resume.experience.skills),
        p(strong("Skill Name: "), resume.skill.name),
        p(strong("Skill Rating: "), resume.skill.rating),
        p(strong("Certification Title: ", resume.certification.title)),
        p(strong("Certification Organization: "), resume.certification.organization),
        p(strong("Certification Year: "), resume.certification.year),
        p(strong("Certification Location: "), resume.certification.location),
      )
    )

  initialize()

object Application extends cask.Main:
  val allRoutes = Seq(
    StaticRoutes(),
    RootRoutes()
  )
  val dbUsername = sys.env.get("DB_USERNAME")
  val dbPassword = sys.env.get("DB_PASSWORD")
  val dbURL =sys.env.get("DB_URL")
  println(s"db username: $dbUsername")
  println(s"db password: $dbPassword")
  println(s"db url: $dbURL")
  
  val flyway = Flyway.configure()
    .dataSource(dbURL.get, dbUsername.get, dbPassword.get)
    .locations("filesystem:./app/src/main/resources/db/migration")
    .load()
  flyway.migrate()

  try
    val conn = DriverManager.getConnection(dbURL.get, dbUsername.get, dbPassword.get)
    val create = DSL.using(conn, SQLDialect.POSTGRES)
    val query = create.select().from(RESUME)
    val values: Result[Record] = query.fetch()
    println(s"values: $values")
  catch
    case e: Exception => println(s"Error fetching data:\n $e")
  finally
    println("Done with database connect")
