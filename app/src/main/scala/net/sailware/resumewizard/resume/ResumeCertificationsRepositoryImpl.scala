package net.sailware.resumewizard.resume

import net.sailware.resumewizard.database.DatabaseResource
import net.sailware.resumewizard.jooq.Tables.RESUME_CERTIFICATIONS
import net.sailware.resumewizard.jooq.tables.records.ResumeCertificationsRecord
import scala.jdk.CollectionConverters.*
import scala.collection.JavaConverters.AsJavaCollection

class ResumeCertificationsRepositoryImpl(databaseResource: DatabaseResource) extends ResumeCertificationsRepository:

  override def fetchCount(): Long =
    databaseResource.ctx.fetchCount(RESUME_CERTIFICATIONS)

  override def fetch(): List[ResumeCertificationsRecord] =
    databaseResource.ctx.selectFrom(RESUME_CERTIFICATIONS)
      .fetchInto(classOf[ResumeCertificationsRecord])
      .asScala
      .toList

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

  override def deleteByExcludedIds(ids: List[Int]): Unit =
    databaseResource.ctx.delete(RESUME_CERTIFICATIONS)
      .where(RESUME_CERTIFICATIONS.ID.notIn(ids.asJava))
      .execute()
