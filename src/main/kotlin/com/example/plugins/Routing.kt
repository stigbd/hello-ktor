package com.example.plugins

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.plugins.openapi.openAPI
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing

fun Application.configureRouting() {
    routing {
        get("/") { call.respondText("Hello World!\n") }
        get("/liveness") { call.response.status(HttpStatusCode.OK) }
        get("/readiness") { call.response.status(HttpStatusCode.OK) }
        openAPI(path = "openapi", swaggerFile = "openapi/documentation.yaml")
    }
}
