package com.example.plugins

import com.example.model.Task
import com.example.model.TaskRepository
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import io.ktor.util.reflect.typeInfo

fun Application.configureSerialization(repository: TaskRepository) {
    install(ContentNegotiation) { json() }
    routing {
        route("/tasks") {
            get {
                val queryParamName = call.request.queryParameters["name"]
                if (queryParamName == null) {
                    val tasks = repository.allTasks()
                    call.respond(tasks, typeInfo = typeInfo<List<Task>>())
                } else {
                    val task = repository.taskByName(queryParamName)
                    if (task == null) {
                        call.response.status(HttpStatusCode.NotFound)
                        return@get
                    }
                    call.respond(task, typeInfo = typeInfo<Task>())
                }
            }
            get("/{id}") {
                val id = call.parameters["id"] ?: return@get
                val task = repository.taskById(id)
                if (task == null) {
                    call.response.status(HttpStatusCode.NotFound)
                    return@get
                }
                call.respond(task, typeInfo = typeInfo<Task>())
            }
        }
    }
}
