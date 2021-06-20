package dev.rudge.application.web

import dev.rudge.application.web.controllers.RiskEvaluationController
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder
import org.koin.standalone.KoinComponent

class Router(
    private val riskEvaluationController: RiskEvaluationController
) : KoinComponent {

    fun register(app: Javalin) {
        app.routes {
            ApiBuilder.post("calculate", riskEvaluationController::calculate)
        }
    }
}
