package net.sailware.resumewizard.resume

import net.sailware.resumewizard.database.DatabaseResource
import net.sailware.resumewizard.jooq.Tables.RESUME_EXPERIENCES
import net.sailware.resumewizard.jooq.tables.records.ResumeExperiencesRecord

class ResumeExperiencesRepositoryImpl(databaseResource: DatabaseResource) extends ResumeExperiencesRepository:

  override def fetchCount(): Long =
    databaseResource.ctx.fetchCount(RESUME_EXPERIENCES)

  override def fetchOne(): ResumeExperiencesRecord =
    databaseResource.ctx.fetchOne(RESUME_EXPERIENCES)

  override def insert(title: String, organization: String, duration: String, location: String, description: String, skills: String): Unit =
    databaseResource.ctx.insertInto(
      RESUME_EXPERIENCES,
      RESUME_EXPERIENCES.TITLE,
      RESUME_EXPERIENCES.ORGANIZATION,
      RESUME_EXPERIENCES.DURATION,
      RESUME_EXPERIENCES.LOCATION,
      RESUME_EXPERIENCES.DESCRIPTION,
      RESUME_EXPERIENCES.SKILLS
    )
      .values(title, organization, duration, location, description, skills)
      .execute()

  override def update(title: String, organization: String, duration: String, location: String, description: String, skills: String): Unit =
    databaseResource.ctx.update(RESUME_EXPERIENCES)
      .set(RESUME_EXPERIENCES.TITLE, title)
      .set(RESUME_EXPERIENCES.ORGANIZATION, organization)
      .set(RESUME_EXPERIENCES.DURATION, duration)
      .set(RESUME_EXPERIENCES.LOCATION, location)
      .set(RESUME_EXPERIENCES.DESCRIPTION, description)
      .set(RESUME_EXPERIENCES.SKILLS, skills)
      .execute()
