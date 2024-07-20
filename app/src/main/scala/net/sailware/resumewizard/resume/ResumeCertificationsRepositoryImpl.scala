package net.sailware.resumewizard.resume

import net.sailware.resumewizard.database.DatabaseResource
import net.sailware.resumewizard.jooq.Tables.RESUME_CERTIFICATIONS
import net.sailware.resumewizard.jooq.tables.records.ResumeCertificationsRecord

class ResumeCertificationsRepositoryImpl(databaseResource: DatabaseResource) extends ResumeCertificationsRepository:

  override def fetchCount(): Long =
    databaseResource.ctx.fetchCount(RESUME_CERTIFICATIONS)

  override def fetchOne(): ResumeCertificationsRecord =
    databaseResource.ctx.fetchOne(RESUME_CERTIFICATIONS)

  override def insert(title: String, organization: String, year: String, location: String): Unit =
    databaseResource.ctx.insertInto(
      RESUME_CERTIFICATIONS,
      RESUME_CERTIFICATIONS.TITLE,
      RESUME_CERTIFICATIONS.ORGANIZATION,
      RESUME_CERTIFICATIONS.YEAR,
      RESUME_CERTIFICATIONS.LOCATION
    )
      .values(title, organization, year, location)
      .execute()

  override def update(id: Int, title: String, organization: String, year: String, location: String): Unit =
    databaseResource.ctx.update(RESUME_CERTIFICATIONS)
      .set(RESUME_CERTIFICATIONS.TITLE, title)
      .set(RESUME_CERTIFICATIONS.ORGANIZATION, organization)
      .set(RESUME_CERTIFICATIONS.YEAR, year)
      .set(RESUME_CERTIFICATIONS.LOCATION, location)
      .where(RESUME_CERTIFICATIONS.ID.eq(id))
      .execute()
