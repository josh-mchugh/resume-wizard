package net.sailware.resumewizard.resume

import net.sailware.resumewizard.database.DatabaseResource
import net.sailware.resumewizard.jooq.Tables.RESUME_DETAILS
import net.sailware.resumewizard.jooq.tables.records.ResumeDetailsRecord
import scala.jdk.OptionConverters.RichOptional

class ResumeContactRepositoryImpl(databaseResource: DatabaseResource) extends ResumeContactRepository:

  override def fetchCount(): Long =
    databaseResource.ctx.fetchCount(RESUME_DETAILS)

  override def fetchOne(): ResumeDetailsRecord =
    databaseResource.ctx.fetchOne(RESUME_DETAILS)

  override def fetchOption(): Option[ResumeDetailsRecord] =
    databaseResource.ctx.fetchOptional(RESUME_DETAILS).asScala

  override def insert(phone: String, email: String, location: String): Unit =
    databaseResource.ctx.insertInto(
      RESUME_DETAILS,
      RESUME_DETAILS.PHONE,
      RESUME_DETAILS.EMAIL,
      RESUME_DETAILS.LOCATION
    )
      .values(phone, email, location)
      .execute()

  override def update(id: Int, phone: String, email: String, location: String): Unit =
    databaseResource.ctx.update(RESUME_DETAILS)
      .set(RESUME_DETAILS.PHONE, phone)
      .set(RESUME_DETAILS.EMAIL, email)
      .set(RESUME_DETAILS.LOCATION, location)
      .where(RESUME_DETAILS.ID.eq(id))
      .execute()
