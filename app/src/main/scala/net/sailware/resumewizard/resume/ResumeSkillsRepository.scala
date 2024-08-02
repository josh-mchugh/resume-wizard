package net.sailware.resumewizard.resume

import net.sailware.resumewizard.database.DatabaseResource
import net.sailware.resumewizard.jooq.Tables.RESUME_SKILLS
import net.sailware.resumewizard.jooq.tables.records.ResumeSkillsRecord

trait ResumeSkillsRepository:

  def fetchCount(): Long
  def fetch(): List[ResumeSkillsRecord]
  def insert(name: String, rating: Short): Unit
  def update(id: Int, name: String, rating: Short): Unit
  def deleteByExcludedIds(ids: List[Int]): Unit
