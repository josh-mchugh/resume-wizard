package net.sailware.resumewizard.resume.wizard.experience.form

import io.undertow.server.handlers.form.FormData
import net.sailware.resumewizard.core.FormUtil

import scala.collection.JavaConverters.asScalaIteratorConverter

case class ExperienceListForm(
  val entries: List[ExperienceUpdateForm],
  val newEntries: List[ExperienceAddForm]
)

object ExperienceListForm:

  def apply(request: cask.Request): ExperienceListForm =
    FormUtil.bind[ExperienceListForm](request, transformBind)  

  private val transformBind = (formData: FormData) =>
    var entries = Map.empty[String, ExperienceUpdateForm]
    var newEntries = Map.empty[String, ExperienceAddForm]
    for key <- formData.iterator().asScala do                            
      key match
        case s"entry[$id].$variable" => entries = handleEntry(entries, id, variable, key, formData)
        case s"newEntry[$id].$variable" => newEntries = handleNewEntry(newEntries, id, variable, key, formData)
        case _ => println("Unable to match form key")
    ExperienceListForm(entries.values.toList, newEntries.values.toList)

  private def handleEntry(result: Map[String, ExperienceUpdateForm], id: String, variable: String, key: String, formData: FormData) =
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
        case "title" => result + (id -> ExperienceUpdateForm(id.toInt, formData.get(key).element().getValue(), "", "", "", "", ""))
        case "organization" => result + (id -> ExperienceUpdateForm(id.toInt, "", formData.get(key).element().getValue(), "", "", "", ""))
        case "duration" => result + (id -> ExperienceUpdateForm(id.toInt, "", "", formData.get(key).element().getValue(), "", "", ""))
        case "location" => result + (id -> ExperienceUpdateForm(id.toInt, "", "", "", formData.get(key).element().getValue(), "", ""))
        case "description" => result + (id -> ExperienceUpdateForm(id.toInt, "", "", "", "", formData.get(key).element().getValue(), ""))
        case "skills" => result + (id -> ExperienceUpdateForm(id.toInt, "", "", "", "", "", formData.get(key).element().getValue()))
        case _ => result
    
  private def handleNewEntry(result: Map[String, ExperienceAddForm], id: String, variable: String, key: String, formData: FormData) =
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
        case "title" => result + (id -> ExperienceAddForm(formData.get(key).element().getValue(), "", "", "", "", ""))
        case "organization" => result + (id -> ExperienceAddForm("", formData.get(key).element().getValue(), "", "", "", ""))
        case "duration" => result + (id -> ExperienceAddForm("", "", formData.get(key).element().getValue(), "", "", ""))
        case "location" => result + (id -> ExperienceAddForm("", "", "", formData.get(key).element().getValue(), "", ""))
        case "description" => result + (id -> ExperienceAddForm("", "", "", "", formData.get(key).element().getValue(), ""))
        case "skills" => result + (id -> ExperienceAddForm("", "", "", "", "", formData.get(key).element().getValue()))
        case _ => result  
