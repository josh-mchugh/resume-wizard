package net.sailware.resumewizard.resume

import net.sailware.resumewizard.database.DatabaseResource
import net.sailware.resumewizard.jooq.Tables.RESUME_SOCIALS
import net.sailware.resumewizard.jooq.tables.records.ResumeSocialsRecord
import scala.jdk.CollectionConverters.*
import scala.jdk.OptionConverters.RichOptional

class ResumeSocialsRepositoryImpl(databaseResource: DatabaseResource) extends ResumeSocialsRepository:

  override def fetchCount(): Long =
    databaseResource.ctx.fetchCount(RESUME_SOCIALS)

  override def fetchOne(): ResumeSocialsRecord =
    databaseResource.ctx.fetchOne(RESUME_SOCIALS)

  override def fetchOption(): Option[ResumeSocialsRecord] =
    databaseResource.ctx.fetchOptional(RESUME_SOCIALS).asScala

  override def fetch(): List[ResumeSocialsRecord] =
    databaseResource.ctx.selectFrom(RESUME_SOCIALS).fetchInto(classOf[ResumeSocialsRecord]).asScala.toList

  override def insert(name: String, url: String): Unit =
    databaseResource.ctx.insertInto(
      RESUME_SOCIALS,
      RESUME_SOCIALS.NAME,
      RESUME_SOCIALS.URL
    )
      .values(name, url)
      .execute()

  override def update(id: Int, name: String, url: String): Unit =
    databaseResource.ctx.update(RESUME_SOCIALS)
      .set(RESUME_SOCIALS.NAME, name)
      .set(RESUME_SOCIALS.URL, url)
      .where(RESUME_SOCIALS.ID.eq(id))
      .execute()
