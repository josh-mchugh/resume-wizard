package com.sailware

import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType

@Path("/")
class GreetingResource():

    @GET
    @Produces(Array(MediaType.TEXT_PLAIN))
    def hello() =
        "Hello World!"
