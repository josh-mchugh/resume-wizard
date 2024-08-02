package net.sailware.resumewizard.resume.wizard.experience.view.model

import io.undertow.server.handlers.form.FormData
import net.sailware.resumewizard.core.FormUtil

import scala.collection.JavaConverters.asScalaIteratorConverter

case class ExperienceEntryListForm(
  val entries: List[ExperienceEntryForm],
  val newEntries: List[ExperienceNewEntryForm]
)

object ExperienceEntryListForm:

  def apply(request: cask.Request): ExperienceEntryListForm =
    FormUtil.bind[ExperienceEntryListForm](request, transformBind)  

  private val transformBind = (formData: FormData) =>
    var entries = Map.empty[String, ExperienceEntryForm]
    var newEntries = Map.empty[String, ExperienceNewEntryForm]
    for key <- formData.iterator().asScala do                            
      key match
        case s"entry[$id].$variable" => entries = handleEntry(entries, id, variable, key, formData)
        case s"newEntry[$id].$variable" => newEntries = handleNewEntry(newEntries, id, variable, key, formData)
        case _ => println("Unable to match form key")
    ExperienceEntryListForm(entries.values.toList, newEntries.values.toList)

  private def handleEntry(result: Map[String, ExperienceEntryForm], id: String, variable: String, key: String, formData: FormData) =
    if result.contains(id) then
      variable match
        case "title" => result + (id -> result(id).copy(title = formData.get(key).element().getValue()))
        case "organization" => result + (id -> result(id).copy(organization = formData.get(key).element().getValue()))
        case "duration" => result + (id -> result(id).copy(duration = formData.get(key).element().getValue()))
        case "location" => result + (id -> result(id).copy(location = formData.get(key).element().getValue()))
        case "description" => result + (id -> result(id).copy(description = formData.get(key).element().getValue()))
        case "skills" => result + (id -> result(id).copy(skills = formData.get(key).element().getValue()))
        case _ => result
    else
      variable match
        case "title" => result + (id -> ExperienceEntryForm(id.toInt, formData.get(key).element().getValue(), "", "", "", "", ""))
        case "organization" => result + (id -> ExperienceEntryForm(id.toInt, "", formData.get(key).element().getValue(), "", "", "", ""))
        case "duration" => result + (id -> ExperienceEntryForm(id.toInt, "", "", formData.get(key).element().getValue(), "", "", ""))
        case "location" => result + (id -> ExperienceEntryForm(id.toInt, "", "", "", formData.get(key).element().getValue(), "", ""))
        case "description" => result + (id -> ExperienceEntryForm(id.toInt, "", "", "", "", formData.get(key).element().getValue(), ""))
        case "skills" => result + (id -> ExperienceEntryForm(id.toInt, "", "", "", "", "", formData.get(key).element().getValue()))
        case _ => result
    
  private def handleNewEntry(result: Map[String, ExperienceNewEntryForm], id: String, variable: String, key: String, formData: FormData) =
    if result.contains(id) then
      variable match
        case "title" => result + (id -> result(id).copy(title = formData.get(key).element().getValue()))
        case "organization" => result + (id -> result(id).copy(organization = formData.get(key).element().getValue()))
        case "duration" => result + (id -> result(id).copy(duration = formData.get(key).element().getValue()))
        case "location" => result + (id -> result(id).copy(location = formData.get(key).element().getValue()))
        case "description" => result + (id -> result(id).copy(description = formData.get(key).element().getValue()))
        case "skills" => result + (id -> result(id).copy(skills = formData.get(key).element().getValue()))
        case _ => result
    else
      variable match
        case "title" => result + (id -> ExperienceNewEntryForm(formData.get(key).element().getValue(), "", "", "", "", ""))
        case "organization" => result + (id -> ExperienceNewEntryForm("", formData.get(key).element().getValue(), "", "", "", ""))
        case "duration" => result + (id -> ExperienceNewEntryForm("", "", formData.get(key).element().getValue(), "", "", ""))
        case "location" => result + (id -> ExperienceNewEntryForm("", "", "", formData.get(key).element().getValue(), "", ""))
        case "description" => result + (id -> ExperienceNewEntryForm("", "", "", "", formData.get(key).element().getValue(), ""))
        case "skills" => result + (id -> ExperienceNewEntryForm("", "", "", "", "", formData.get(key).element().getValue()))
        case _ => result  
