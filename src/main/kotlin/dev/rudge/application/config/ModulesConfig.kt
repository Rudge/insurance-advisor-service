package dev.rudge.application.config

import dev.rudge.application.web.Router
import dev.rudge.application.web.controllers.RiskEvaluationController
import dev.rudge.domain.service.RiskEvaluationService
import org.koin.dsl.module.module

object ModulesConfig {
    private val configModule = module {
        single { AppConfig() }
        single { Router(get()) }
    }
    private val riskModule = module {
        single { RiskEvaluationController(get()) }
        single { RiskEvaluationService() }
    }

    internal val allModules = listOf(
        configModule, riskModule
    )
}
