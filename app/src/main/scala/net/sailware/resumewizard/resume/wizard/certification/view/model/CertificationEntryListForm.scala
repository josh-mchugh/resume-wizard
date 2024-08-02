package net.sailware.resumewizard.resume.wizard.certification.view.model

import io.undertow.server.handlers.form.FormData
import net.sailware.resumewizard.core.FormUtil
import scala.collection.JavaConverters.asScalaIteratorConverter

case class CertificationEntryListForm(
  val entries: List[CertificationEntryForm],
  val newEntries: List[CertificationNewEntryForm]
)

object CertificationEntryListForm:

  def apply(request: cask.Request): CertificationEntryListForm =
    FormUtil.bind[CertificationEntryListForm](request, transformBind)

  private val transformBind = (formData: FormData) =>
    var entries = Map.empty[String, CertificationEntryForm]
    var newEntries = Map.empty[String, CertificationNewEntryForm]
    for key <- formData.iterator().asScala do                            
      key match
        case s"entry[$id].$variable" => entries = handleEntry(entries, id, variable, key, formData)
        case s"newEntry[$id].$variable" => newEntries = handleNewEntry(newEntries, id, variable, key, formData)
        case _ => println("Unable to match form key")
    CertificationEntryListForm(entries.values.toList, newEntries.values.toList)

  private def handleEntry(result: Map[String, CertificationEntryForm], id: String, variable: String, key: String, formData: FormData) =
    if result.contains(id) then
      variable match
        case "title" => result + (id -> result(id).copy(title = formData.get(key).element().getValue()))
        case "organization" => result + (id -> result(id).copy(organization = formData.get(key).element().getValue()))
        case "year" => result + (id -> result(id).copy(year = formData.get(key).element().getValue()))
        case "location" => result + (id -> result(id).copy(location = formData.get(key).element().getValue()))
        case _ => result
    else
      variable match
        case "title" => result + (id -> CertificationEntryForm(id.toInt, formData.get(key).element().getValue(), "", "", ""))
        case "organization" => result + (id -> CertificationEntryForm(id.toInt, "", formData.get(key).element().getValue(), "", ""))
        case "year" => result + (id -> CertificationEntryForm(id.toInt, "", "", formData.get(key).element().getValue(), ""))
        case "location" => result + (id -> CertificationEntryForm(id.toInt, "", "", "", formData.get(key).element().getValue()))
        case _ => result
    
  private def handleNewEntry(result: Map[String, CertificationNewEntryForm], id: String, variable: String, key: String, formData: FormData) =
    if result.contains(id) then
      variable match
        case "title" => result + (id -> result(id).copy(title = formData.get(key).element().getValue()))
        case "organization" => result + (id -> result(id).copy(organization = formData.get(key).element().getValue()))
        case "year" => result + (id -> result(id).copy(year = formData.get(key).element().getValue()))
        case "location" => result + (id -> result(id).copy(location = formData.get(key).element().getValue()))
        case _ => result
    else
      variable match
        case "title" => result + (id -> CertificationNewEntryForm(formData.get(key).element().getValue(), "", "", ""))
        case "organization" => result + (id -> CertificationNewEntryForm("", formData.get(key).element().getValue(), "", ""))
        case "year" => result + (id -> CertificationNewEntryForm("", "", formData.get(key).element().getValue(), ""))
        case "location" => result + (id -> CertificationNewEntryForm("", "", "", formData.get(key).element().getValue()))
        case _ => result  
