package net.sailware.resumewizard.resume

import net.sailware.resumewizard.jooq.tables.records.ResumeSkillsRecord

case class Skill(
  val id: Int,
  val name: String,
  val rating: Short
)

object Skill:

  def apply(record: ResumeSkillsRecord): Skill =
    Skill(record.getId(), record.getName(), record.getRating())
