package net.sailware.resumewizard.resume

import net.sailware.resumewizard.jooq.tables.records.ResumeDetailsRecord

trait ResumeContactRepository:

  def fetchCount(): Long
  def fetchOne(): ResumeDetailsRecord
  def fetchOption(): Option[ResumeDetailsRecord]
  def insert(phone: String, email: String, location: String): Unit
  def update(id: Int, phone: String, email: String, location: String): Unit
