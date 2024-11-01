package com.example

import com.example.model.Task
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.testing.testApplication
import kotlin.test.Test
import kotlin.test.assertEquals

class TasksTest {
    // val postgreSQLContainer =
    //     PostgreSQLContainer<Nothing>("postgres:17").apply {
    //         withReuse(true)
    //         start()
    //     }

    @Test
    fun testGetAllTasks() =
        testApplication {
            application { module() }
            val client =
                createClient {
                    install(ContentNegotiation) {
                        json()
                    }
                }
            val response = client.get("tasks")
            assertEquals(HttpStatusCode.OK, response.status)
            assertEquals("application/json; charset=UTF-8", response.headers["Content-Type"])
            // Test that the response body is a list of length 3:
            val tasks = response.body<List<Task>>()
            assertEquals(3, tasks.size)
        }

    @Test
    fun testGetTaskById() =
        testApplication {
            application { module() }
            val client =
                createClient {
                    install(ContentNegotiation) {
                        json()
                    }
                }
            val tasks = client.get("tasks").body<List<Task>>()
            val response = client.get("tasks/${tasks.first().id}")
            assertEquals(HttpStatusCode.OK, response.status)
            assertEquals("application/json; charset=UTF-8", response.headers["Content-Type"])
        }

    @Test
    fun testGetTaskByName() =
        testApplication {
            application { module() }
            val client =
                createClient {
                    install(ContentNegotiation) {
                        json()
                    }
                }
            val tasks = client.get("tasks").body<List<Task>>()
            val response = client.get("tasks?name=${tasks.first().name}")
            assertEquals(HttpStatusCode.OK, response.status)
            assertEquals("application/json; charset=UTF-8", response.headers["Content-Type"])
        }

    @Test
    fun testGetTaskByNameNotFound() =
        testApplication {
            application { module() }
            val response = client.get("tasks?name=Task 4")
            assertEquals(HttpStatusCode.NotFound, response.status)
        }

    @Test
    fun testGetTaskByIdNotFound() =
        testApplication {
            application { module() }
            val response = client.get("tasks/4")
            assertEquals(HttpStatusCode.NotFound, response.status)
        }
}
