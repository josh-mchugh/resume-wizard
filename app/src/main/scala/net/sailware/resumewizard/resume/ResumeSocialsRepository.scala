package net.sailware.resumewizard.resume

import net.sailware.resumewizard.database.DatabaseResource
import net.sailware.resumewizard.jooq.Tables.RESUME_SOCIALS
import net.sailware.resumewizard.jooq.tables.records.ResumeSocialsRecord

trait ResumeSocialsRepository:

  def fetchCount(): Long
  def fetchOne(): ResumeSocialsRecord
  def insert(name: String, url: String): Unit
  def update(name: String, url: String): Unit