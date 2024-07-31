package net.sailware.resumewizard.resume.wizard.detail.form

import io.undertow.server.handlers.form.FormData
import net.sailware.resumewizard.core.FormUtil
import scala.collection.JavaConverters.asScalaIteratorConverter

case class DetailForm(
  val name: String,
  val title: String,
  val summary: String,
  val phone: String,
  val email: String,
  val location: String
)

object DetailForm:

  def apply(request: cask.Request): DetailForm =
    FormUtil.bind[DetailForm](request, transformBind)

  private val transformBind = (formData: FormData) =>
    var details = DetailForm("", "", "", "", "", "")
    for key <- formData.iterator().asScala do                            
      key match
        case "name" => details = details.copy(name = formData.get(key).element().getValue())
        case "title" => details = details.copy(title = formData.get(key).element().getValue())
        case "summary" => details = details.copy(summary = formData.get(key).element().getValue())
        case "phone" => details = details.copy(phone = formData.get(key).element().getValue())
        case "email" => details = details.copy(email = formData.get(key).element().getValue())
        case "location" => details = details.copy(location = formData.get(key).element().getValue())
        case _ => println("Unable to match form key")
    details
