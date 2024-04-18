package com.sailware

import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import io.quarkus.qute.{Template, Location}

@Path("/")
class GreetingResource(@Location("greeting.html")private val template: Template):
    @GET
    @Produces(Array(MediaType.TEXT_HTML))
    def hello() =
        template.data("name", "Quarkus")
