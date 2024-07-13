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
import net.sailware.resumewizard.jooq.tables.records.*

import scala.jdk.OptionConverters.RichOptional

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
      buildPage(buildSteps(Step.Detail), form)
    else
      val form = buildForm(Step.Detail, buildNameAndTitleForm("", "", ""))
      buildPage(buildSteps(Step.Detail), form) 

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
      buildPage(buildSteps(Step.Contact), form)
    else
      val form = buildForm(Step.Contact, buildContactsForm("", "", ""))
      buildPage(buildSteps(Step.Contact), form) 

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
      buildPage(buildSteps(Step.Social), form)
    else
      val form = buildForm(Step.Social, buildSocialsForm("", ""))
      buildPage(buildSteps(Step.Social), form) 

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
      buildPage(buildSteps(Step.Experience), form)
    else
      val form = buildForm(Step.Experience, buildExperienceForm("", "", "", "", "", ""))
      buildPage(buildSteps(Step.Experience), form) 

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
    if dslContext.fetchCount(RESUME_SKILLS) > 0 then
      val result = dslContext.fetchOne(RESUME_SKILLS)
      val form = buildForm(Step.Skill, buildSkillForm(result.getName(), result.getRating()))
      buildPage(buildSteps(Step.Skill), form)
    else
      val form = buildForm(Step.Skill, buildSkillForm("", 0))
      buildPage(buildSteps(Step.Skill), form) 

  @cask.postForm("/wizard/skill")
  def postWizardSkill(name: String, rating: Short) =
    if dslContext.fetchCount(RESUME_SKILLS) > 0 then
      val resumeDetail = dslContext.selectFrom(RESUME_SKILLS).fetchOne()
      dslContext.update(RESUME_SKILLS)
        .set(RESUME_SKILLS.NAME, name)
        .set(RESUME_SKILLS.RATING, rating)
        .execute()
    else
      dslContext.insertInto(RESUME_SKILLS, RESUME_SKILLS.NAME, RESUME_SKILLS.RATING)
        .values(name, rating)
        .execute()
    cask.Redirect("/wizard/certification")

  @cask.get("/wizard/certification")
  def getWizardCertification() =
    if dslContext.fetchCount(RESUME_CERTIFICATIONS) > 0 then
      val result = dslContext.fetchOne(RESUME_CERTIFICATIONS)
      val form = buildForm(Step.Certification, buildCertificationForm(result.getTitle(), result.getOrganization(), result.getYear(), result.getLocation()))
      buildPage(buildSteps(Step.Certification), form)
    else
      val form = buildForm(Step.Certification, buildCertificationForm("", "", "", ""))
      buildPage(buildSteps(Step.Certification), form)

  @cask.postForm("/wizard/certification")
  def postWizardCertification(title: String, organization: String, year: String, location: String) =
    if dslContext.fetchCount(RESUME_CERTIFICATIONS) > 0 then
      val resumeDetail = dslContext.selectFrom(RESUME_CERTIFICATIONS).fetchOne()
      dslContext.update(RESUME_CERTIFICATIONS)
        .set(RESUME_CERTIFICATIONS.TITLE, title)
        .set(RESUME_CERTIFICATIONS.ORGANIZATION, organization)
        .set(RESUME_CERTIFICATIONS.YEAR, year)
        .set(RESUME_CERTIFICATIONS.LOCATION, location)
        .execute()
    else
      dslContext.insertInto(RESUME_CERTIFICATIONS, RESUME_CERTIFICATIONS.TITLE, RESUME_CERTIFICATIONS.ORGANIZATION, RESUME_CERTIFICATIONS.YEAR, RESUME_CERTIFICATIONS.LOCATION)
        .values(title, organization, year, location)
        .execute()
    cask.Redirect("/wizard/review")

  @cask.get("/wizard/review")
  def getWizardReview() =
    val detailsOption = dslContext.fetchOptional(RESUME_DETAILS).toScala
    val socialsOption = dslContext.fetchOptional(RESUME_SOCIALS).toScala
    val experiencesOption = dslContext.fetchOptional(RESUME_EXPERIENCES).toScala
    val skillsOption = dslContext.fetchOptional(RESUME_SKILLS).toScala
    val certificationsOption = dslContext.fetchOptional(RESUME_CERTIFICATIONS).toScala
    buildPage(buildSteps(Step.Review), buildReview(detailsOption, socialsOption, experiencesOption, skillsOption, certificationsOption))

  def buildPage(steps: Frag, content: Frag) =
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

  def buildSkillForm(skillName: String, rating: Short) =
    List(
      div()(
        label(cls := "form-label")("Name"),
        input(cls := "form-control", `type` := "text", name := "name", placeholder := "Name", value := skillName)
      ),
      div(cls := "mt-3")(
        label(cls := "form-label")("Rating"),
        input(cls := "form-range", `type` := "range", name := "rating", min := 0, max := 5, step := 1, value := rating)
      )
    )

  def buildCertificationForm(title: String, organization: String, year: String, location: String) =
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
        label(cls := "form-label")("Year"),
        input(cls := "form-control", `type` := "text", name := "year", placeholder := "Year", value := year)
      ),
      div(cls := "mt-3")(
        label(cls := "form-label")("Location"),
        input(cls := "form-control", `type` := "text", name := "location", placeholder := "Location", value := location)
      )
    )

  def buildActions(current: Step) =
    div(cls := "wizardly-actions")(
      if hasNext(current) then button(cls := "btn btn-primary", `type` := "submit")("Next") else ""
    )

  def hasNext(step: Step) =
    step.ordinal != Step.values.length - 1

  def buildReview(detailsOption: Option[ResumeDetailsRecord], socialsOption: Option[ResumeSocialsRecord], experiencesOption: Option[ResumeExperiencesRecord], skillsOption: Option[ResumeSkillsRecord], certificationsOption: Option[ResumeCertificationsRecord]) =
    val details = List(
        p(strong("Name: "), detailsOption.get.getName()),
        p(strong("Title: "), detailsOption.get.getTitle()),
        p(strong("Summary: "), detailsOption.get.getSummary()),
        p(strong("Phone: "), detailsOption.get.getPhone()),
        p(strong("Email: "), detailsOption.get.getEmail()),
        p(strong("Location: "), detailsOption.get.getLocation()),
    )

    val socials = List(
      p(strong("Social Name: "), socialsOption.get.getName()),
      p(strong("Social URL: "), socialsOption.get.getUrl()),
    )

    val experiences = List(
      p(strong("Experience Title: "), experiencesOption.get.getTitle()),
      p(strong("Experience Organization: "), experiencesOption.get.getOrganization()),
      p(strong("Experience Duration: "), experiencesOption.get.getDuration()),
      p(strong("Experience Location: "), experiencesOption.get.getLocation()),
      p(strong("Experience Description: "), experiencesOption.get.getDescription()),
      p(strong("Experience Skills: "), experiencesOption.get.getSkills()),
    )

    val skills = List(
      p(strong("Skill Name: "), skillsOption.get.getName()),
      p(strong("Skill Rating: "), skillsOption.get.getRating().toString()),
    )

    val certifications = List(
      p(strong("Certification Title: ", certificationsOption.get.getTitle())),
      p(strong("Certification Organization: "), certificationsOption.get.getOrganization()),
      p(strong("Certification Year: "), certificationsOption.get.getYear()),
      p(strong("Certification Location: "), certificationsOption.get.getLocation()),
    )

    div()(
      div(id := "main")(
        if detailsOption.isDefined then details else frag(),
        if socialsOption.isDefined then socials else frag(),
        if experiencesOption.isDefined then experiences else frag(),
        if skillsOption.isDefined then skills else frag(),
        if certificationsOption.isDefined then certifications else frag()
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
  











