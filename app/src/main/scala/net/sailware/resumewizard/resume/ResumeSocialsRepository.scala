package net.sailware.resumewizard.resume

import net.sailware.resumewizard.database.DatabaseResource
import net.sailware.resumewizard.jooq.Tables.RESUME_SOCIALS
import net.sailware.resumewizard.jooq.tables.records.ResumeSocialsRecord

trait ResumeSocialsRepository:

  def fetch(): List[ResumeSocialsRecord]
  def insert(name: String, url: String): Unit
  def update(id: Int, name: String, url: String): Unit
  def deleteByExcludedIds(ids: List[Int]): Unit
