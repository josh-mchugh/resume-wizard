package net.sailware.resumewizard.resume.wizard

import io.undertow.server.handlers.form.FormData
import net.sailware.resumewizard.core.FormUtil

import scala.collection.JavaConverters.asScalaIteratorConverter

object ResumeSocialFormUtil:

  def bind(request: cask.Request): SocialListForm =
    FormUtil.bind[SocialListForm](request, transformBind)

  private val transformBind = (formData: FormData) =>
    var result = Map.empty[String, SocialForm]
    for key <- formData.iterator().asScala do
      result = key match
        case s"form[$i].$variable" =>
          if result.contains(i) then
            variable match
              case "name" => result + (i -> result(i).copy(name = formData.get(key).element().getValue()))
              case "url" => result + (i -> result(i).copy(url = formData.get(key).element().getValue()))
              case _ => result
          else
            variable match
              case "name" => result + (i -> SocialForm(formData.get(key).element().getValue(), ""))
              case "url" => result + (i -> SocialForm("", formData.get(key).element().getValue()))
              case _ => result
        case _ => result
    SocialListForm(result.values.toList)
