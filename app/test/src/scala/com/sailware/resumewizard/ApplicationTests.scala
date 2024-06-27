package com.sailware.resumewizard

import io.undertow.Undertow
import utest.*

object ApplicationTests extends TestSuite:
  def withServer[T](app: cask.main.Main)(f: String => T): T =
    val server = Undertow.builder
      .addHttpListener(8081, "localhost")
      .setHandler(app.defaultHandler)
      .build
    server.start()
    val res =
      try f("http://localhost:8081")
      finally server.stop
    res

  val tests = Tests {
    test("Application Tests") - withServer(Application) { host =>
      val success = requests.get(host)
      success.statusCode ==> 200
    }
  }
