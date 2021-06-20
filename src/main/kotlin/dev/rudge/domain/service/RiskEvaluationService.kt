package dev.rudge.domain.service

import dev.rudge.domain.entities.RiskEvaluation
import dev.rudge.domain.entities.UserInformation

class RiskEvaluationService(
    private val autoRiskScoreService: AutoRiskScoreService,
    private val disabilityRiskScoreService: DisabilityRiskScoreService,
    private val homeRiskScoreService: HomeRiskScoreService,
    private val lifeRiskScoreService: LifeRiskScoreService
) {
    fun calculate(userInformation: UserInformation): RiskEvaluation {
        validateRequiredInfo(userInformation)

        return RiskEvaluation(
            autoRiskScoreService.calculate(userInformation),
            disabilityRiskScoreService.calculate(userInformation),
            homeRiskScoreService.calculate(userInformation),
            lifeRiskScoreService.calculate(userInformation)
        )
    }

    private fun validateRequiredInfo(userInformation: UserInformation) {
        require(userInformation.age >= 0) { "Invalid age, must be equal or greater than 0!" }
        require(userInformation.dependents >= 0) { "Invalid dependents, must be equal or greater than 0!" }
        require(userInformation.income >= 0) { "Invalid income, must be equal or greater than 0!" }
        require(userInformation.riskQuestions.size == 3) { "Invalid risk answers, must have 3 answers!" }
    }
}