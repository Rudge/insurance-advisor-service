package dev.rudge.application.config

import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
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
import org.koin.core.KoinProperties
import org.koin.dsl.module.module
import org.koin.log.EmptyLogger
import org.koin.standalone.KoinComponent
import org.koin.standalone.StandAloneContext
import org.koin.standalone.getProperty
import org.koin.standalone.inject

object AppConfig : KoinComponent {
    private val riskEvaluationController: RiskEvaluationController by inject()

    fun setup(): Javalin {
        StandAloneContext.startKoin(
            listOf(riskModule),
            KoinProperties(true, true),
            logger = EmptyLogger()
        )
        this.configureMapper()
        val app = Javalin.create { config ->
            config.apply {
                enableWebjars()
                contextPath = getProperty("context")
                addStaticFiles("/swagger")
                addSinglePageRoot("", "/swagger/swagger-ui.html")
                server {
                    Server(getProperty("server_port") as Int)
                }
            }
        }.events {
            it.serverStopping {
                StandAloneContext.stopKoin()
            }
        }
        register(app)
        return app
    }

    private fun register(app: Javalin) {
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
    }

    private fun configureMapper() {
        JavalinJackson.configure(
            jacksonObjectMapper().setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
        )
    }
}

