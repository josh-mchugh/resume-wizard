package net.sailware.resumewizard.resume

import net.sailware.resumewizard.database.DatabaseResource
import net.sailware.resumewizard.jooq.Tables.RESUME_DETAILS
import net.sailware.resumewizard.jooq.tables.records.ResumeDetailsRecord

trait ResumeDetailsRepository:

  def fetchOne(): ResumeDetailsRecord
  def fetchCount(): Long
  def insert(name: String, title: String, summary: String): Unit
  def update(name: String, title: String, summary: String): Unit
