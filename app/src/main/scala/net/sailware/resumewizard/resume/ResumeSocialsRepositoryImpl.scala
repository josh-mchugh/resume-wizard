package net.sailware.resumewizard.resume

import net.sailware.resumewizard.database.DatabaseResource
import net.sailware.resumewizard.jooq.Tables.RESUME_SOCIALS
import net.sailware.resumewizard.jooq.tables.records.ResumeSocialsRecord

class ResumeSocialsRepositoryImpl(databaseResource: DatabaseResource) extends ResumeSocialsRepository:

  override def fetchCount(): Long =
    databaseResource.ctx.fetchCount(RESUME_SOCIALS)

  override def fetchOne(): ResumeSocialsRecord =
    databaseResource.ctx.fetchOne(RESUME_SOCIALS)

  override def insert(name: String, url: String): Unit =
    databaseResource.ctx.insertInto(
      RESUME_SOCIALS,
      RESUME_SOCIALS.NAME,
      RESUME_SOCIALS.URL
    )
      .values(name, url)
      .execute()

  override def update(name: String, url: String): Unit =
    databaseResource.ctx.update(RESUME_SOCIALS)
        .set(RESUME_SOCIALS.NAME, name)
        .set(RESUME_SOCIALS.URL, url)
        .execute()