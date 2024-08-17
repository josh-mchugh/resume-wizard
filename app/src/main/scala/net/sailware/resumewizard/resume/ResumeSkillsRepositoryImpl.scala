package net.sailware.resumewizard.resume

import net.sailware.resumewizard.database.DatabaseResource
import net.sailware.resumewizard.jooq.Tables.RESUME_SKILLS
import net.sailware.resumewizard.jooq.tables.records.ResumeSkillsRecord
import scala.jdk.CollectionConverters.*
import scala.collection.JavaConverters.AsJavaCollection

class ResumeSkillsRepositoryImpl(databaseResource: DatabaseResource) extends ResumeSkillsRepository:

  override def fetchCount(): Long =
    databaseResource.ctx.fetchCount(RESUME_SKILLS)

  override def fetch(): List[Skill] =
    databaseResource.ctx.selectFrom(RESUME_SKILLS)
      .fetchInto(classOf[ResumeSkillsRecord])
      .asScala
      .toList
      .map(record => Skill(record))

  override def insert(name: String, rating: Int): Unit =
    databaseResource.ctx.insertInto(
      RESUME_SKILLS,
      RESUME_SKILLS.NAME,
      RESUME_SKILLS.RATING
    )
      .values(name, rating)
      .execute()

  override def update(id: Int, name: String, rating: Int): Unit =
    databaseResource.ctx.update(RESUME_SKILLS)
      .set(RESUME_SKILLS.NAME, name)
      .set(RESUME_SKILLS.RATING, rating)
      .where(RESUME_SKILLS.ID.eq(id))
      .execute()

  override def deleteByExcludedIds(ids: List[Int]): Unit =
    databaseResource.ctx.delete(RESUME_SKILLS)
      .where(RESUME_SKILLS.ID.notIn(ids.asJava))
      .execute()
