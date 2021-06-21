package dev.rudge.application.config

import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import dev.rudge.application.web.controllers.ErrorExceptionMapping
import dev.rudge.application.web.controllers.RiskEvaluationController
import dev.rudge.domain.service.AutoRiskScoreService
import dev.rudge.domain.service.DisabilityRiskScoreService
import dev.rudge.domain.service.HomeRiskScoreService
import dev.rudge.domain.service.LifeRiskScoreService
import dev.rudge.domain.service.RiskEvaluationService
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder
import io.javalin.plugin.json.JavalinJackson
import org.eclipse.jetty.server.Server
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.context.GlobalContext.stopKoin
import org.koin.core.logger.EmptyLogger
import org.koin.dsl.module
import org.koin.fileProperties

object AppConfig : KoinComponent {
    private val properties: Map<String, String> by inject()

    fun setup(): Javalin {
        startKoin {
            modules(riskModule)
            logger(EmptyLogger())
            fileProperties()
        }
        this.configureMapper()
        val app = Javalin.create { config ->
            config.apply {
                enableWebjars()
                addStaticFiles("/swagger")
                addSinglePageRoot("", "/swagger/swagger-ui.html")

                properties["context"]?.apply { contextPath = this }
                properties["server_port"]?.apply {
                    server {
                        Server(this.toInt())
                    }
                }
            }
        }.events {
            it.serverStopping {
                stopKoin()
            }
        }
        ErrorExceptionMapping.register(app)
        register(app)
        return app
    }

    private fun register(app: Javalin) {
        val riskEvaluationController: RiskEvaluationController by inject()
        app.routes {
            ApiBuilder.post("calculate", riskEvaluationController::calculate)
        }
    }

    private val riskModule = module {
        single { RiskEvaluationController(get()) }
        single { RiskEvaluationService(get(), get(), get(), get()) }
        single { AutoRiskScoreService() }
        single { DisabilityRiskScoreService() }
        single { HomeRiskScoreService() }
        single { LifeRiskScoreService() }
        single {
            mapOf(
                "context" to getProperty("context"),
                "server_port" to getProperty("server_port")
            )
        }
    }

    private fun configureMapper() {
        JavalinJackson.configure(
            jacksonObjectMapper().setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
        )
    }
}

