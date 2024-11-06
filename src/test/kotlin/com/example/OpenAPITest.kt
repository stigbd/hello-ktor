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
import kotlin.test.assertTrue

class OpenAPITest : ApplicationTest() {
    @Test
    fun `get of openapi returns an openAPI specification`() =
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
            val response = client.get("/openapi")
            assertEquals(HttpStatusCode.OK, response.status)
            // Check if the response contains the Content-Type header
            assertTrue(response.headers.contains("Content-Type"))
            // Check if the content-type is text/html:
            assertTrue(response.headers["Content-Type"]?.contains("text/html") ?: false)
            assertTrue(response.bodyAsText().contains("Hello Ktor API"))
            assertTrue(response.bodyAsText().contains("API and SDK Documentation"))
        }
}
