package net.sailware.resumewizard.resume

import net.sailware.resumewizard.jooq.tables.records.ResumeDetailsRecord

case class Detail(
  val id: Int,
  val name: String,
  val title: String,
  val summary: String,
  val phone: String,
  val email: String,
  val location: String
)

object Detail:

  def apply(record: ResumeDetailsRecord): Detail =
    Detail(
      record.getId(),
      record.getName(),
      record.getTitle(),
      record.getSummary(),
      record.getPhone(),
      record.getEmail(),
      record.getLocation()
    )

  def apply(): Detail =
    Detail(0, "", "", "", "", "", "")
