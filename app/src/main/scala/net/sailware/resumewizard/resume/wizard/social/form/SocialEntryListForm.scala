package net.sailware.resumewizard.resume.wizard.social.form

import io.undertow.server.handlers.form.FormData
import net.sailware.resumewizard.core.FormUtil

import scala.collection.JavaConverters.asScalaIteratorConverter

case class SocialEntryListForm(
  val entries: List[SocialEntryForm],
  val newEntries: List[SocialNewEntryForm]
)

object SocialListForm:

  def apply(request: cask.Request): SocialEntryListForm =
    FormUtil.bind[SocialEntryListForm](request, transformBind)

  private val transformBind = (formData: FormData) =>
    var entries = Map.empty[String, SocialEntryForm]
    var newEntries = Map.empty[String, SocialNewEntryForm]
    for key <- formData.iterator().asScala do                            
      key match
        case s"entry[$id].$variable" => entries = handleEntry(entries, id, variable, key, formData)
        case s"newEntry[$id].$variable" => newEntries = handleNewEntry(newEntries, id, variable, key, formData)
        case _ => println("Unable to match form key")
    SocialEntryListForm(entries.values.toList, newEntries.values.toList)

  private def handleEntry(result: Map[String, SocialEntryForm], id: String, variable: String, key: String, formData: FormData) =
    if result.contains(id) then
      variable match
        case "name" => result + (id -> result(id).copy(name = formData.get(key).element().getValue()))
        case "url" => result + (id -> result(id).copy(url = formData.get(key).element().getValue()))
        case _ => result
    else
      variable match
        case "name" => result + (id -> SocialEntryForm(id.toInt, formData.get(key).element().getValue(), ""))
        case "url" => result + (id -> SocialEntryForm(id.toInt, "", formData.get(key).element().getValue()))
        case _ => result
    
  private def handleNewEntry(result: Map[String, SocialNewEntryForm], id: String, variable: String, key: String, formData: FormData) =
    if result.contains(id) then
      variable match
        case "name" => result + (id -> result(id).copy(name = formData.get(key).element().getValue()))
        case "url" => result + (id -> result(id).copy(url = formData.get(key).element().getValue()))
        case _ => result
    else
      variable match
        case "name" => result + (id -> SocialNewEntryForm(formData.get(key).element().getValue(), ""))
        case "url" => result + (id -> SocialNewEntryForm("", formData.get(key).element().getValue()))
        case _ => result  
