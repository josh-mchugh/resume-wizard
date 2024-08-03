package net.sailware.resumewizard.resume

import net.sailware.resumewizard.database.DatabaseResource
import net.sailware.resumewizard.jooq.Tables.RESUME_EXPERIENCES
import net.sailware.resumewizard.jooq.tables.records.ResumeExperiencesRecord

trait ResumeExperiencesRepository:

  def fetchCount(): Long  
  def fetch(): List[Experience]
  def insert(title: String, organization: String, duration: String, location: String, description: String, skills: String): Unit
  def update(id: Int, title: String, organization: String, duration: String, location: String, description: String, skills: String): Unit
  def deleteByExcludedIds(id: List[Int]): Unit
