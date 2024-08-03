package net.sailware.resumewizard

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

object Application extends cask.Main:

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
  
