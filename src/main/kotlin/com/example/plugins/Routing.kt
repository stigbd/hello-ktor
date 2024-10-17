package com.example.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.openapi.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        get("/") { call.respondText("Hello World!\n") }
        get("/liveness") { call.response.status(HttpStatusCode.OK) }
        get("/readiness") { call.response.status(HttpStatusCode.OK) }
        openAPI(path = "openapi", swaggerFile = "openapi/documentation.yaml")
    }
}
