package net.sailware.resumewizard.resume

import net.sailware.resumewizard.database.DatabaseResource
import net.sailware.resumewizard.jooq.Tables.RESUME_SKILLS
import net.sailware.resumewizard.jooq.tables.records.ResumeSkillsRecord

trait ResumeSkillsRepository:

  def fetchCount(): Long
  def fetch(): List[Skill]
  def insert(name: String, rating: Int): Unit
  def update(id: Int, name: String, rating: Int): Unit
  def deleteByExcludedIds(ids: List[Int]): Unit
