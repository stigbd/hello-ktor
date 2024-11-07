package com.example

import com.example.model.PostgresTaskRepository
import com.example.plugins.configureDatabases
import com.example.plugins.configureFlyway
import com.example.plugins.configureRouting
import com.example.plugins.configureSerialization
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import kotlin.test.Test
import kotlin.test.assertEquals

class RootTest : ApplicationTest() {
    @Test
    fun `get of root returns OK and a greeting text`() =
        testApplication {
            environment {
                config = postgreSQLconfig
            }
            application {
                val repository = PostgresTaskRepository()

                configureDatabases(environment.config)
                configureSerialization(repository)
                configureFlyway(environment.config)
                configureRouting()
            }
            val response = client.get("/")
            assertEquals(HttpStatusCode.OK, response.status)
            assertEquals("text/plain; charset=UTF-8", response.headers["Content-Type"])
            assertEquals("Hello World!\n", response.bodyAsText())
        }
}
