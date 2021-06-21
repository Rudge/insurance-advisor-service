package dev.rudge.app

import dev.rudge.application.config.AppConfig
import io.restassured.http.ContentType
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

class InsuranceAdvisorClientTest {

    companion object {
        private val app = AppConfig.setup()

        @BeforeAll
        @JvmStatic
        fun beforeAll() {
            app.start()
        }

        @AfterAll
        @JvmStatic
        fun afterAll() {
            app.stop()
        }
    }


    @Test
    fun `given a request to evaluate risk profile with income zero when post to endpoint should return the final score, auto ineligible, disability ineligible, home ineligible and life regular`() {
        Given {
            port(app.port())
            contentType(ContentType.JSON)
            accept(ContentType.JSON)
            body(
                """
                {
                  "age": 35,
                  "dependents": 2,
                  "house": {"ownership_status": "owned"},
                  "income": 0,
                  "marital_status": "married",
                  "risk_questions": [0, 1, 0],
                  "vehicle": {"year": 2018}
                }
            """.trimIndent()
            )
        } When {
            post("/api/calculate")
        } Then {
            statusCode(200)
            body("auto", equalTo("ineligible"))
            body("disability", equalTo("ineligible"))
            body("home", equalTo("ineligible"))
            body("life", equalTo("regular"))
        }
    }

    @Test
    fun `given a request to evaluate risk profile with income more than zero when post to endpoint should return the final score, auto regular, disability economic, home economic and life regular`() {
        Given {
            port(app.port())
            contentType(ContentType.JSON)
            accept(ContentType.JSON)
            body(
                """
                {
                  "age": 35,
                  "dependents": 2,
                  "house": {"ownership_status": "owned"},
                  "income": 1,
                  "marital_status": "married",
                  "risk_questions": [0, 1, 0],
                  "vehicle": {"year": 2018}
                }
            """.trimIndent()
            )
        } When {
            post("/api/calculate")
        } Then {
            statusCode(200)
            body("auto", equalTo("regular"))
            body("disability", equalTo("economic"))
            body("home", equalTo("economic"))
            body("life", equalTo("regular"))
        }
    }
}