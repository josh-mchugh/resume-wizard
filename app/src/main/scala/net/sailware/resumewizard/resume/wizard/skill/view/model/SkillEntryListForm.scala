package net.sailware.resumewizard.resume.wizard.skill.view.model

import io.undertow.server.handlers.form.FormData
import net.sailware.resumewizard.core.FormUtil
import scala.collection.JavaConverters.asScalaIteratorConverter

case class SkillEntryListForm(
  val entries: List[SkillEntryForm],
  val newEntries: List[SkillNewEntryForm]
)

object SkillEntryListForm:

  def apply(request: cask.Request): SkillEntryListForm =
    FormUtil.bind[SkillEntryListForm](request, transformBind)

  private val transformBind = (formData: FormData) =>
    var entries = Map.empty[String, SkillEntryForm]
    var newEntries = Map.empty[String, SkillNewEntryForm]
    for key <- formData.iterator().asScala do                            
      key match
        case s"entry[$id].$variable" => entries = handleEntry(entries, id, variable, key, formData)
        case s"newEntry[$id].$variable" => newEntries = handleNewEntry(newEntries, id, variable, key, formData)
        case _ => println("Unable to match form key")
    SkillEntryListForm(entries.values.toList, newEntries.values.toList)

  private def handleEntry(result: Map[String, SkillEntryForm], id: String, variable: String, key: String, formData: FormData) =
    if result.contains(id) then
      variable match
        case "name" => result + (id -> result(id).copy(name = formData.get(key).element().getValue()))
        case "rating" => result + (id -> result(id).copy(rating = formData.get(key).element().getValue().toInt))
        case _ => result
    else
      variable match
        case "name" => result + (id -> SkillEntryForm(id.toInt, formData.get(key).element().getValue(), 0))
        case "rating" => result + (id -> SkillEntryForm(id.toInt, "", formData.get(key).element().getValue().toInt))
        case _ => result
    
  private def handleNewEntry(result: Map[String, SkillNewEntryForm], id: String, variable: String, key: String, formData: FormData) =
    if result.contains(id) then
      variable match
        case "name" => result + (id -> result(id).copy(name = formData.get(key).element().getValue()))
        case "rating" => result + (id -> result(id).copy(rating = formData.get(key).element().getValue().toInt))
        case _ => result
    else
      variable match
        case "name" => result + (id -> SkillNewEntryForm(formData.get(key).element().getValue(), 0))
        case "rating" => result + (id -> SkillNewEntryForm("", formData.get(key).element().getValue().toInt))
        case _ => result  
