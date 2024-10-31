package com.example.plugins

import com.example.model.Task
import com.example.model.TaskRepository
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import io.ktor.util.reflect.typeInfo
import kotlinx.serialization.*
import kotlinx.serialization.json.*

fun Application.configureSerialization(repository: TaskRepository) {
    install(ContentNegotiation) { json() }
    routing {
        route("/tasks") {
            get {
                val tasks = repository.allTasks()
                call.respond(tasks, typeInfo = typeInfo<List<Task>>())
            }

            get("/byName/{taskName}") {
                val name = call.parameters["taskName"]
                if (name == null) {
                    call.respond(message = "", typeInfo = typeInfo<String>())
                    return@get
                }
                val task = repository.taskByName(name)
                if (task == null) {
                    call.respond(HttpStatusCode.NotFound, typeInfo = typeInfo<String>())
                    return@get
                }
                call.respond(task, typeInfo = typeInfo<Task>())
            }
        }
    }
}
