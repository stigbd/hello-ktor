package com.example

import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ApplicationTest {
    @Test
    fun testRoot() =
        testApplication {
            application { module() }
            val response = client.get("/")
            assertEquals(HttpStatusCode.OK, response.status)
            assertEquals("text/plain; charset=UTF-8", response.headers["Content-Type"])
            assertEquals("Hello World!\n", response.bodyAsText())
        }

    @Test
    fun testLiveness() =
        testApplication {
            application { module() }
            val response = client.get("/liveness")
            assertEquals(HttpStatusCode.OK, response.status)
        }

    @Test
    fun testReadiness() =
        testApplication {
            application { module() }
            val response = client.get("/readiness")
            assertEquals(HttpStatusCode.OK, response.status)
        }

    @Test
    fun testOpenAPI() =
        testApplication {
            application { module() }
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
