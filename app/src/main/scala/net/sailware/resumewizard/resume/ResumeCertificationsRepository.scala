package net.sailware.resumewizard.resume

import net.sailware.resumewizard.jooq.Tables.RESUME_CERTIFICATIONS
import net.sailware.resumewizard.jooq.tables.records.ResumeCertificationsRecord

trait ResumeCertificationsRepository:

  def fetchCount(): Long
  def fetchOne(): ResumeCertificationsRecord
  def fetchOption(): Option[ResumeCertificationsRecord]
  def insert(title: String, organization: String, year: String, location: String): Unit
  def update(id: Int, title: String, organization: String, year: String, location: String): Unit
