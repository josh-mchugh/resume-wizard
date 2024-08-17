package net.sailware.resumewizard

import cask.main.Main
import javafx.concurrent as jfxc
import io.undertow.Undertow
import net.sailware.resumewizard.config.ConfigServiceImpl
import net.sailware.resumewizard.core.StaticRoutes
import net.sailware.resumewizard.database.DatabaseResourceImpl
import net.sailware.resumewizard.database.FlywayServiceImpl
import net.sailware.resumewizard.resume.ResumeCertificationsRepositoryImpl
import net.sailware.resumewizard.resume.ResumeExperiencesRepositoryImpl
import net.sailware.resumewizard.resume.ResumeDetailsRepositoryImpl
import net.sailware.resumewizard.resume.ResumeSkillsRepositoryImpl
import net.sailware.resumewizard.resume.ResumeSocialsRepositoryImpl
import net.sailware.resumewizard.resume.wizard.certification.ResumeCertificationRoutes
import net.sailware.resumewizard.resume.wizard.experience.ResumeExperienceRoutes
import net.sailware.resumewizard.resume.wizard.detail.ResumeDetailsRoutes
import net.sailware.resumewizard.resume.wizard.review.ResumeReviewRoutes
import net.sailware.resumewizard.resume.wizard.skill.ResumeSkillRoutes
import net.sailware.resumewizard.resume.wizard.social.ResumeSocialRoutes
import scalafx.application.JFXApp3
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.concurrent.Task
import scalafx.scene.Scene
import scalafx.scene.layout.Priority
import scalafx.scene.web.WebView

object Application extends JFXApp3:
  object Server extends cask.main.Main:

    val configService = ConfigServiceImpl()

    val flywayService = FlywayServiceImpl(configService)
    flywayService.migrate()

    val databaseResource = DatabaseResourceImpl(configService)

    val certificationsRepository = ResumeCertificationsRepositoryImpl(databaseResource)
    val detailsRepository = ResumeDetailsRepositoryImpl(databaseResource)
    val experiencesRepository = ResumeExperiencesRepositoryImpl(databaseResource)
    val skillsRepository = ResumeSkillsRepositoryImpl(databaseResource)
    val socialsRepository = ResumeSocialsRepositoryImpl(databaseResource) 

    val allRoutes = Seq(
      StaticRoutes(),
      ResumeCertificationRoutes(certificationsRepository),
      ResumeDetailsRoutes(detailsRepository),
      ResumeExperienceRoutes(experiencesRepository),
      ResumeReviewRoutes(
        certificationsRepository,
        detailsRepository,
        experiencesRepository,
        skillsRepository,
        socialsRepository
      ),
      ResumeSkillRoutes(skillsRepository),
      ResumeSocialRoutes(socialsRepository),
    )

    override def port: Int = 31337

    override def host: String = "localhost"

    val server = Undertow.builder
      .addHttpListener(port, host)
      .setHandler(defaultHandler)
      .build

    def start() =
      server.start()

  object Worker extends Task(new jfxc.Task[Unit] {
    protected def call(): Unit =
      Server.start()
  })

  override def start(): Unit =
    new Thread(Worker).start()
    val browser = new WebView {
      hgrow = Priority.Always
      vgrow = Priority.Always
      contextMenuEnabled = false
    }
    val engine = browser.engine
    engine.load("http://localhost:31337/wizard/detail")
    stage = new PrimaryStage {
      title = "Resume Wizard"
      width = 800
      height = 600
      scene = new Scene {
        root = browser
      }
    }
