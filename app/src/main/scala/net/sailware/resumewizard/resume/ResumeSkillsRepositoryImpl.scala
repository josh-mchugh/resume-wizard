package net.sailware.resumewizard.resume

import net.sailware.resumewizard.database.DatabaseResource
import net.sailware.resumewizard.jooq.Tables.RESUME_SKILLS
import net.sailware.resumewizard.jooq.tables.records.ResumeSkillsRecord

class ResumeSkillsRepositoryImpl(databaseResource: DatabaseResource) extends ResumeSkillsRepository:

  override def fetchCount(): Long =
    databaseResource.ctx.fetchCount(RESUME_SKILLS)

  override def fetchOne(): ResumeSkillsRecord =
    databaseResource.ctx.fetchOne(RESUME_SKILLS)

  override def insert(name: String, rating: Short): Unit =
    databaseResource.ctx.insertInto(
      RESUME_SKILLS,
      RESUME_SKILLS.NAME,
      RESUME_SKILLS.RATING
    )
      .values(name, rating)
      .execute()

  override def update(id: Int, name: String, rating: Short): Unit =
    databaseResource.ctx.update(RESUME_SKILLS)
      .set(RESUME_SKILLS.NAME, name)
      .set(RESUME_SKILLS.RATING, rating)
      .where(RESUME_SKILLS.ID.eq(id))
      .execute()
