package net.sailware.resumewizard.resume.wizard.social

import io.undertow.server.handlers.form.FormData
import net.sailware.resumewizard.core.FormUtil
import net.sailware.resumewizard.resume.wizard.social.form.SocialAddForm
import net.sailware.resumewizard.resume.wizard.social.form.SocialUpdateForm
import net.sailware.resumewizard.resume.wizard.social.form.SocialListForm

import scala.collection.JavaConverters.asScalaIteratorConverter

object ResumeSocialFormUtil:

  def bind(request: cask.Request): SocialListForm =
    FormUtil.bind[SocialListForm](request, transformBind)

  private val transformBind = (formData: FormData) =>
    var entries = Map.empty[String, SocialUpdateForm]
    var newEntries = Map.empty[String, SocialAddForm]
    for key <- formData.iterator().asScala do                            
      key match
        case s"entry[$id].$variable" => entries = handleEntry(entries, id, variable, key, formData)
        case s"newEntry[$id].$variable" => newEntries = handleNewEntry(newEntries, id, variable, key, formData)
        case _ => println("Unable to match form key")
    SocialListForm(entries.values.toList, newEntries.values.toList)

  private def handleEntry(result: Map[String, SocialUpdateForm], id: String, variable: String, key: String, formData: FormData) =
    if result.contains(id) then
      variable match
        case "name" => result + (id -> result(id).copy(name = formData.get(key).element().getValue()))
        case "url" => result + (id -> result(id).copy(url = formData.get(key).element().getValue()))
        case _ => result
    else
      variable match
        case "name" => result + (id -> SocialUpdateForm(id.toInt, formData.get(key).element().getValue(), ""))
        case "url" => result + (id -> SocialUpdateForm(id.toInt, "", formData.get(key).element().getValue()))
        case _ => result
    
  private def handleNewEntry(result: Map[String, SocialAddForm], id: String, variable: String, key: String, formData: FormData) =
    if result.contains(id) then
      variable match
        case "name" => result + (id -> result(id).copy(name = formData.get(key).element().getValue()))
        case "url" => result + (id -> result(id).copy(url = formData.get(key).element().getValue()))
        case _ => result
    else
      variable match
        case "name" => result + (id -> SocialAddForm(formData.get(key).element().getValue(), ""))
        case "url" => result + (id -> SocialAddForm("", formData.get(key).element().getValue()))
        case _ => result  
