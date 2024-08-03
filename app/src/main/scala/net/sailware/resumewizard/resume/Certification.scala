package net.sailware.resumewizard.resume

import net.sailware.resumewizard.jooq.tables.records.ResumeCertificationsRecord

case class Certification(
  val id: Int,
  val title: String,
  val organization: String,
  val year: String,
  val location: String
)

object Certification:

  def apply(record: ResumeCertificationsRecord): Certification =
    Certification(
      record.getId(),
      record.getTitle(),
      record.getOrganization(),
      record.getYear(),
      record.getLocation()
    )
