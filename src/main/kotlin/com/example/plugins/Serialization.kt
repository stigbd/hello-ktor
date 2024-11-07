package com.example.plugins

import com.example.model.Task
import com.example.model.TaskRepository
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.JsonConvertException
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.request.receive
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import io.ktor.util.reflect.typeInfo
import java.util.UUID

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
                    val tasks = repository.tasksByName(queryParamName)
                    call.respond(tasks, typeInfo = typeInfo<List<Task>>())
                }
            }
            post {
                try {
                    val task = call.receive<Task>()
                    // Check for empty name:
                    if (task.name.isEmpty()) {
                        call.response.status(HttpStatusCode.BadRequest)
                        return@post
                    }
                    // Check for duplicate name:
                    if (repository.tasksByName(task.name).size > 0) {
                        call.response.status(HttpStatusCode.BadRequest)
                        return@post
                    }
                    repository.addTask(task)
                    call.response.status(HttpStatusCode.Created)
                    return@post
                } catch (ex: JsonConvertException) {
                    call.response.status(HttpStatusCode.BadRequest)
                    return@post
                }
            }
            get("/{id}") {
                val id = call.parameters["id"] ?: return@get
                val uuid = UUID.fromString(id)
                val task = repository.taskById(uuid)
                if (task == null) {
                    call.response.status(HttpStatusCode.NotFound)
                    return@get
                }
                call.respond(task, typeInfo = typeInfo<Task>())
            }
        }
    }
}
