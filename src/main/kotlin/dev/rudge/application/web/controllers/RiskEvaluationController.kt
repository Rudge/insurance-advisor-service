package dev.rudge.application.web.controllers

import dev.rudge.domain.entities.PersonalInformation
import dev.rudge.domain.service.RiskEvaluationService
import io.javalin.http.Context

class RiskEvaluationController(private val riskEvaluationService: RiskEvaluationService) {

    fun calculate(ctx: Context) {
        ctx.bodyValidator(PersonalInformation::class.java).get().apply {
            ctx.json(riskEvaluationService.calculate(this))
        }
    }
}