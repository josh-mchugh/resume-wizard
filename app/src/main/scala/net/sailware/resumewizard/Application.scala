package net.sailware.resumewizard

import net.sailware.resumewizard.config.ConfigServiceImpl
import net.sailware.resumewizard.database.DatabaseResourceImpl
import net.sailware.resumewizard.database.FlywayServiceImpl
import net.sailware.resumewizard.resume.ResumeContactRepositoryImpl
import net.sailware.resumewizard.resume.ResumeDetailsRepositoryImpl
import net.sailware.resumewizard.resume.ResumeSocialsRepositoryImpl
import net.sailware.resumewizard.resume.wizard.ResumeCertificationRoutes
import net.sailware.resumewizard.resume.wizard.ResumeContactRoutes
import net.sailware.resumewizard.resume.wizard.ResumeDetailsRoutes
import net.sailware.resumewizard.resume.wizard.ResumeExperienceRoutes
import net.sailware.resumewizard.resume.wizard.ResumeReviewRoutes
import net.sailware.resumewizard.resume.wizard.ResumeSkillRoutes
import net.sailware.resumewizard.resume.wizard.ResumeSocialRoutes
import net.sailware.resumewizard.static.StaticRoutes

object Application extends cask.Main:

  val configService = ConfigServiceImpl()

  val flywayService = FlywayServiceImpl(configService)
  flywayService.migrate()

  val databaseResource = DatabaseResourceImpl(configService)

  val allRoutes = Seq(
    StaticRoutes(),
    ResumeCertificationRoutes(databaseResource),
    ResumeContactRoutes(ResumeContactRepositoryImpl(databaseResource)),
    ResumeDetailsRoutes(ResumeDetailsRepositoryImpl(databaseResource)),
    ResumeExperienceRoutes(databaseResource),
    ResumeReviewRoutes(databaseResource),
    ResumeSkillRoutes(databaseResource),
    ResumeSocialRoutes(ResumeSocialsRepositoryImpl(databaseResource)),
  )
  











