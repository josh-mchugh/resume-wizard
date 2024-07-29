package net.sailware.resumewizard.resume.wizard

import io.undertow.server.handlers.form.FormData
import net.sailware.resumewizard.core.FormUtil

import scala.collection.JavaConverters.asScalaIteratorConverter

object ResumeSocialFormUtil:

  def bind(request: cask.Request): SocialListForm =
    FormUtil.bind[SocialListForm](request, transformBind)

  private val transformBind = (formData: FormData) =>
    var entries = Map.empty[String, SocialForm]
    var newEntries = Map.empty[String, SocialNewForm]
    for key <- formData.iterator().asScala do                            
      key match
        case s"entry[$id].$variable" => entries = handleEntry(entries, id, variable, key, formData)
        case s"newEntry[$id].$variable" => newEntries = handleNewEntry(newEntries, id, variable, key, formData)
        case _ => println("Unable to match form key")
    SocialListForm(entries.values.toList, newEntries.values.toList)

  private def handleEntry(result: Map[String, SocialForm], id: String, variable: String, key: String, formData: FormData) =
    if result.contains(id) then
      variable match
        case "name" => result + (id -> result(id).copy(name = formData.get(key).element().getValue()))
        case "url" => result + (id -> result(id).copy(url = formData.get(key).element().getValue()))
        case _ => result
    else
      variable match
        case "name" => result + (id -> SocialForm(id.toInt, formData.get(key).element().getValue(), ""))
        case "url" => result + (id -> SocialForm(id.toInt, "", formData.get(key).element().getValue()))
        case _ => result
    
  private def handleNewEntry(result: Map[String, SocialNewForm], id: String, variable: String, key: String, formData: FormData) =
    if result.contains(id) then
      variable match
        case "name" => result + (id -> result(id).copy(name = formData.get(key).element().getValue()))
        case "url" => result + (id -> result(id).copy(url = formData.get(key).element().getValue()))
        case _ => result
    else
      variable match
        case "name" => result + (id -> SocialNewForm(formData.get(key).element().getValue(), ""))
        case "url" => result + (id -> SocialNewForm("", formData.get(key).element().getValue()))
        case _ => result  
