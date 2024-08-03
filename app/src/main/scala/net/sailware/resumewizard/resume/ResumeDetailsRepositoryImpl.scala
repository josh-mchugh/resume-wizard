package net.sailware.resumewizard.resume

import net.sailware.resumewizard.database.DatabaseResource
import net.sailware.resumewizard.jooq.Tables.RESUME_DETAILS
import net.sailware.resumewizard.jooq.tables.records.ResumeDetailsRecord
import scala.jdk.OptionConverters.RichOptional

class ResumeDetailsRepositoryImpl(databaseResource: DatabaseResource) extends ResumeDetailsRepository:

  override def fetchCount(): Long =
    databaseResource.ctx.fetchCount(RESUME_DETAILS)

  override def fetchOne(): Detail =
    val record = databaseResource.ctx.fetchOne(RESUME_DETAILS)
    Detail(record)

  override def fetchOption(): Option[Detail] =
    databaseResource.ctx.fetchOptional(RESUME_DETAILS).asScala
      .map(record => Detail(record))

  override def insert(name: String, title: String, summary: String, phone: String, email: String, location: String): Unit =
    databaseResource.ctx.insertInto(
      RESUME_DETAILS,
      RESUME_DETAILS.NAME,
      RESUME_DETAILS.TITLE,
      RESUME_DETAILS.SUMMARY,
      RESUME_DETAILS.PHONE,
      RESUME_DETAILS.EMAIL,
      RESUME_DETAILS.LOCATION
    )
      .values(name, title, summary, phone, email, location)
      .execute()

  override def update(id: Int, name: String, title: String, summary: String, phone: String, email: String, location: String): Unit =
    databaseResource.ctx.update(RESUME_DETAILS)
      .set(RESUME_DETAILS.NAME, name)
      .set(RESUME_DETAILS.TITLE, title)
      .set(RESUME_DETAILS.SUMMARY, summary)
      .set(RESUME_DETAILS.PHONE, phone)
      .set(RESUME_DETAILS.EMAIL, email)
      .set(RESUME_DETAILS.LOCATION, location)
      .where(RESUME_DETAILS.ID.eq(id))
      .execute()
