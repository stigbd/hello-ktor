package com.example

import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import org.testcontainers.containers.PostgreSQLContainer
import kotlin.test.Test
import kotlin.test.assertEquals

class TasksTest {
    val postgreSQLContainer =
        PostgreSQLContainer<Nothing>("postgres:17").apply {
            withReuse(true)
            start()
        }

    @Test
    fun testGetAllTasks() =
        testApplication {
            application { module() }
            val response = client.get("tasks")
            assertEquals(HttpStatusCode.OK, response.status)
            assertEquals("application/json; charset=UTF-8", response.headers["Content-Type"])
        }
}
