package net.sailware.resumewizard.core

import io.undertow.server.handlers.form.FormData
import io.undertow.server.handlers.form.FormParserFactory

object FormUtil:

  def bind[T](request: cask.Request, transform: FormData => T): T =
    val formData = FormParserFactory.builder().build().createParser(request.exchange).parseBlocking()
    transform(formData)
