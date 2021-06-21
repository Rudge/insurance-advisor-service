package dev.rudge.domain.service

import dev.rudge.domain.entities.InsuranceScore
import dev.rudge.domain.entities.MaritalStatus
import dev.rudge.domain.entities.UserInformation

class DisabilityRiskScoreService : RiskScoreService {
    override fun calculate(userInformation: UserInformation) = when {
        userInformation.income == 0 -> InsuranceScore.INELIGIBLE
        userInformation.vehicle == null -> InsuranceScore.INELIGIBLE
        userInformation.house == null -> InsuranceScore.INELIGIBLE
        userInformation.age > 60 -> InsuranceScore.INELIGIBLE
        else -> {
            var score = userInformation.getBaseScore()
            score = calculateScoreByAge(userInformation, score)
            score = calculateScoreByIncome(userInformation, score)
            score = calculateScoreByHouse(userInformation.house, score)
            score = calculateScoreByDependents(userInformation, score)
            score = calculateScoreByMaritalStatus(userInformation, score)
            InsuranceScore.getByScore(score)
        }
    }

    private fun calculateScoreByMaritalStatus(
        userInformation: UserInformation,
        score: Int
    ) = takeIf { userInformation.maritalStatus == MaritalStatus.MARRIED }?.let {
        score - 1
    } ?: score

}