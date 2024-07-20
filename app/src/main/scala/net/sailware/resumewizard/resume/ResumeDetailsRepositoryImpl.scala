package net.sailware.resumewizard.resume

import net.sailware.resumewizard.database.DatabaseResource
import net.sailware.resumewizard.jooq.Tables.RESUME_DETAILS
import net.sailware.resumewizard.jooq.tables.records.ResumeDetailsRecord
import scala.jdk.OptionConverters.RichOptional

class ResumeDetailsRepositoryImpl(databaseResource: DatabaseResource) extends ResumeDetailsRepository:

  override def fetchCount(): Long =
    databaseResource.ctx.fetchCount(RESUME_DETAILS)

  override def fetchOne(): ResumeDetailsRecord =
    databaseResource.ctx.fetchOne(RESUME_DETAILS)

  override def fetchOption(): Option[ResumeDetailsRecord] =
    databaseResource.ctx.fetchOptional(RESUME_DETAILS).asScala

  override def insert(name: String, title: String, summary: String): Unit =
    databaseResource.ctx.insertInto(
      RESUME_DETAILS,
      RESUME_DETAILS.NAME,
      RESUME_DETAILS.TITLE,
      RESUME_DETAILS.SUMMARY
    )
      .values(name, title, summary)
      .execute()

  override def update(id: Int, name: String, title: String, summary: String): Unit =
    databaseResource.ctx.update(RESUME_DETAILS)
      .set(RESUME_DETAILS.NAME, name)
      .set(RESUME_DETAILS.TITLE, title)
      .set(RESUME_DETAILS.SUMMARY, summary)
      .where(RESUME_DETAILS.ID.eq(id))
      .execute()
