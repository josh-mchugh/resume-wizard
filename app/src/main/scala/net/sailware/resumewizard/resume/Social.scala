package net.sailware.resumewizard.resume

import net.sailware.resumewizard.jooq.tables.records.ResumeSocialsRecord

case class Social(
  id: Int,
  name: String,
  url: String
)

object Social:

  def apply(record: ResumeSocialsRecord): Social =
    Social(record.getId(), record.getName(), record.getUrl())
