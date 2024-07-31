package net.sailware.resumewizard.resume.wizard.detail.view.model

import net.sailware.resumewizard.jooq.tables.records.ResumeDetailsRecord

case class Detail(
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
      record.getName(),
      record.getTitle(),
      record.getSummary(),
      record.getPhone(),
      record.getEmail(),
      record.getLocation()
    )

  def apply(): Detail =
    Detail("", "", "", "", "", "")
