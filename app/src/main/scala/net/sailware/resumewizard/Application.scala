package net.sailware.resumewizard

import scalatags.Text.all.*
import scalatags.Text.tags2.nav
import scalatags.Text.tags2.section
import scalatags.Text.tags2.title
import java.sql.{Connection, DriverManager}
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.flywaydb.core.Flyway
import org.jooq.DSLContext
import org.jooq.Result
import org.jooq.Record
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import net.sailware.resumewizard.jooq.Tables.*

case class StaticRoutes()(implicit cc: castor.Context, log: cask.Logger) extends cask.Routes:

  @cask.staticFiles("/static/")
  def staticFilesRoutes() = "app/src/main/resources/public"

  initialize()

case class RootRoutes(dslContext: DSLContext)(implicit cc: castor.Context, log: cask.Logger) extends cask.Routes:
  enum Step(val label: String):
    case Detail extends Step("Name & Title")
    case Contact extends Step("Contacts")
    case Social extends Step("Socials")
    case Experience extends Step("Experiences")
    case Skill extends Step("Skills")
    case Certification extends Step("Certifications")
    case Review extends Step("Review")

  @cask.get("/wizard/detail")
  def getWizardName() =
    if dslContext.fetchCount(RESUME_DETAILS) > 0 then
      val result = dslContext.fetchOne(RESUME_DETAILS)
      val form = buildForm(Step.Detail, buildNameAndTitleForm(result.getName(), result.getTitle(), result.getSummary()))
      buildPage2(buildSteps(Step.Detail), form)
    else
      val form = buildForm(Step.Detail, buildNameAndTitleForm("", "", ""))
      buildPage2(buildSteps(Step.Detail), form) 

  @cask.postForm("/wizard/detail")
  def postWizardName(name: String, title: String, summary: String) =
    if dslContext.fetchCount(RESUME_DETAILS) > 0 then
      val resumeDetail = dslContext.selectFrom(RESUME_DETAILS).fetchOne()
      dslContext.update(RESUME_DETAILS)
        .set(RESUME_DETAILS.NAME, name)
        .set(RESUME_DETAILS.TITLE, title)
        .set(RESUME_DETAILS.SUMMARY, summary)
        .execute()
    else
      dslContext.insertInto(RESUME_DETAILS, RESUME_DETAILS.NAME, RESUME_DETAILS.TITLE, RESUME_DETAILS.SUMMARY)
        .values(name, title, summary)
        .execute()
    cask.Redirect("/wizard/contact")

  @cask.get("/wizard/contact")
  def getWizardContact() =
    if dslContext.fetchCount(RESUME_DETAILS) > 0 then
      val result = dslContext.fetchOne(RESUME_DETAILS)
      val form = buildForm(Step.Contact, buildContactsForm(result.getPhone(), result.getEmail(), result.getLocation()))
      buildPage2(buildSteps(Step.Contact), form)
    else
      val form = buildForm(Step.Contact, buildContactsForm("", "", ""))
      buildPage2(buildSteps(Step.Contact), form) 

  @cask.postForm("/wizard/contact")
  def postWizardContact(phone: String, email: String, location: String) =
    if dslContext.fetchCount(RESUME_DETAILS) > 0 then
      val resumeDetail = dslContext.selectFrom(RESUME_DETAILS).fetchOne()
      dslContext.update(RESUME_DETAILS)
        .set(RESUME_DETAILS.PHONE, phone)
        .set(RESUME_DETAILS.EMAIL, email)
        .set(RESUME_DETAILS.LOCATION, location)
        .execute()
    else
      dslContext.insertInto(RESUME_DETAILS, RESUME_DETAILS.PHONE, RESUME_DETAILS.EMAIL, RESUME_DETAILS.LOCATION)
        .values(phone, email, location)
        .execute()
    cask.Redirect("/wizard/social")

  @cask.get("/wizard/social")
  def getWizardSocial() =
    if dslContext.fetchCount(RESUME_SOCIALS) > 0 then
      val result = dslContext.fetchOne(RESUME_SOCIALS)
      val form = buildForm(Step.Social, buildSocialsForm(result.getName(), result.getUrl()))
      buildPage2(buildSteps(Step.Social), form)
    else
      val form = buildForm(Step.Social, buildSocialsForm("", ""))
      buildPage2(buildSteps(Step.Social), form) 

  @cask.postForm("/wizard/social")
  def postWizardSocial(name: String, url: String) =
    if dslContext.fetchCount(RESUME_SOCIALS) > 0 then
      val resumeDetail = dslContext.selectFrom(RESUME_SOCIALS).fetchOne()
      dslContext.update(RESUME_SOCIALS)
        .set(RESUME_SOCIALS.NAME, name)
        .set(RESUME_SOCIALS.URL, url)
        .execute()
    else
      dslContext.insertInto(RESUME_SOCIALS, RESUME_SOCIALS.NAME, RESUME_SOCIALS.URL)
        .values(name, url)
        .execute()
    cask.Redirect("/wizard/experience")

  @cask.get("/wizard/experience")
  def getWizardExperience() =
    if dslContext.fetchCount(RESUME_EXPERIENCES) > 0 then
      val result = dslContext.fetchOne(RESUME_EXPERIENCES)
      val form = buildForm(Step.Experience, buildExperienceForm(result.getTitle(), result.getOrganization(), result.getDuration(), result.getLocation(), result.getDescription(), result.getSkills()))
      buildPage2(buildSteps(Step.Experience), form)
    else
      val form = buildForm(Step.Experience, buildExperienceForm("", "", "", "", "", ""))
      buildPage2(buildSteps(Step.Experience), form) 

  @cask.postForm("/wizard/experience")
  def postWizardExperience(title: String, organization: String, duration: String, location: String, description: String, skills: String) =
    if dslContext.fetchCount(RESUME_EXPERIENCES) > 0 then
      val resumeDetail = dslContext.selectFrom(RESUME_EXPERIENCES).fetchOne()
      dslContext.update(RESUME_EXPERIENCES)
        .set(RESUME_EXPERIENCES.TITLE, title)
        .set(RESUME_EXPERIENCES.ORGANIZATION, organization)
        .set(RESUME_EXPERIENCES.DURATION, duration)
        .set(RESUME_EXPERIENCES.LOCATION, location)
        .set(RESUME_EXPERIENCES.DESCRIPTION, description)
        .set(RESUME_EXPERIENCES.SKILLS, skills)
        .execute()
    else
      dslContext.insertInto(RESUME_EXPERIENCES, RESUME_EXPERIENCES.TITLE, RESUME_EXPERIENCES.ORGANIZATION, RESUME_EXPERIENCES.DURATION, RESUME_EXPERIENCES.LOCATION, RESUME_EXPERIENCES.DESCRIPTION, RESUME_EXPERIENCES.SKILLS)
        .values(title, organization, duration, location, description, skills)
        .execute()
    cask.Redirect("/wizard/skill")

  @cask.get("/wizard/skill")
  def getWizardSkill() =
    buildPage(Step.Skill)

  @cask.postForm("/wizard/skill")
  def postWizardSkill(name: String, rating: Int) =
    cask.Redirect("/wizard/certification")

  @cask.get("/wizard/certification")
  def getWizardCertification() =
    buildPage(Step.Certification)

  @cask.postForm("/wizard/certification")
  def postWizardCertification(title: String, organization: String, year: String, location: String) =
    cask.Redirect("/wizard/review")

  @cask.get("/wizard/review")
  def getWizardReview() =
    buildPage(Step.Review)

  def buildPage2(steps: Frag, content: Frag) =
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
                          steps,
                          content,
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
      case Step.Detail => buildForm(step, buildNameAndTitleForm("", "", ""))
      case Step.Contact => buildForm(step, buildContactsForm("", "", ""))
      case Step.Social => buildForm(step, buildSocialsForm("", ""))
      case Step.Experience => buildForm(step, buildExperienceForm("", "", "", "", "", ""))
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

  def buildNameAndTitleForm(resumeName: String, title: String, summary: String) =
    List(
      div()(
        label(cls := "form-label")("Name"),
        input(cls := "form-control", `type` := "text", name := "name", placeholder := "Name", value := resumeName)
      ),
      div(cls := "mt-3")(
        label(cls := "form-label")("Title"),
        input(cls := "form-control", `type` := "text", name := "title", placeholder := "Title", value := title)
      ),
      div(cls := "mt-3")(
        label(cls := "form-label")("Summary"),
        textarea(cls := "form-control", rows := 3, name := "summary",  placeholder := "Summary of your current or previous role")(summary)
      )
    )

  def buildContactsForm(phone: String, email: String, location: String) =
    List(
      div()(
        label(cls := "form-label")("Phone Number"),
        input(cls := "form-control", `type` := "text", name := "phone", placeholder := "Phone Number", value := phone)
      ),
      div(cls := "mt-3")(
        label(cls := "form-label")("Email Address"),
        input(cls := "form-control", `type` := "text", name := "email", placeholder := "Email Address", value := email)
      ),
      div(cls := "mt-3")(
        label(cls := "form-label")("Location"),
        input(cls := "form-control", `type` := "text", name := "location", placeholder := "Location", value := location)
      )
    )

  def buildSocialsForm(socialName: String, url: String) =
    List(
      div()(
        label(cls := "form-label")("Name"),
        input(cls := "form-control", `type` := "text", name := "name", placeholder := "Name", value := socialName)
      ),
      div(cls := "mt-3")(
        label(cls := "form-label")("URL"),
        input(cls := "form-control", `type` := "text", name := "url", placeholder := "URL", value := url)
      )
    )

  def buildExperienceForm(title: String, organization: String, duration: String, location: String, description: String, skills: String) =
    List(
      div()(
        label(cls := "form-label")("Title"),
        input(cls := "form-control", `type` := "text", name := "title", placeholder := "Title", value := title)
      ),
      div(cls := "mt-3")(
        label(cls := "form-label")("Organization"),
        input(cls := "form-control", `type` := "text", name := "organization", placeholder := "Organization", value := organization)
      ),
      div(cls := "mt-3")(
        label(cls := "form-label")("Duration"),
        input(cls := "form-control", `type` := "text", name := "duration",  placeholder := "Duration", value := duration)
      ),
      div(cls := "mt-3")(
        label(cls := "form-label")("Location"),
        input(cls := "form-control", `type` := "text", name := "location", placeholder := "Location", value := location)
      ),
      div(cls := "mt-3")(
        label(cls := "form-label")("Description"),
        textarea(cls := "form-control", rows := 3, name := "description", placeholder := "Description")(description)
      ),
      div(cls := "mt-3")(
        label(cls := "form-label")("Skills"),
        textarea(cls := "form-control", rows := 3, name := "skills",  placeholder := "Skills")(skills)
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
/*        p(strong("Name: "), resume.name),
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
*/
      )
    )

  initialize()

case class DatabaseConfig(url: String, username: String, password: String)

object Application extends cask.Main:
  // Get database configuration from environment variables
  val dbUsername = sys.env.get("DB_USERNAME")
  val dbPassword = sys.env.get("DB_PASSWORD")
  val dbURL =sys.env.get("DB_URL")
  val databaseConfig = DatabaseConfig(dbURL.get, dbUsername.get, dbPassword.get)

  // Run Flyway database migrations
  val flyway = Flyway.configure()
    .dataSource(databaseConfig.url, databaseConfig.username, databaseConfig.password)
    .locations("filesystem:./app/src/main/resources/db/migration")
    .validateMigrationNaming(true)
    .load()
  flyway.migrate()

  // Setup HikariCP for database connection pool
  val hikariConfig = new HikariConfig()
  hikariConfig.setJdbcUrl(databaseConfig.url)
  hikariConfig.setUsername(databaseConfig.username)
  hikariConfig.setPassword(databaseConfig.password)

  val dslContext = DSL.using(new HikariDataSource(hikariConfig), SQLDialect.POSTGRES)

  val allRoutes = Seq(
    StaticRoutes(),
    RootRoutes(dslContext)
  )
  











