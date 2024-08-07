package net.sailware.resumewizard.resume

import net.sailware.resumewizard.database.DatabaseResource
import net.sailware.resumewizard.jooq.Tables.RESUME_DETAILS
import net.sailware.resumewizard.jooq.tables.records.ResumeDetailsRecord

trait ResumeDetailsRepository:

  def fetchCount(): Long
  def fetchOne(): Detail
  def fetchOption(): Option[Detail]
  def insert(name: String, title: String, summary: String, phone: String, email: String, location: String): Unit
  def update(id: Int, name: String, title: String, summary: String, phone: String, email: String, location: String): Unit
