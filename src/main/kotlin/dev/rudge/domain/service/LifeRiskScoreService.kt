package dev.rudge.domain.service

import dev.rudge.domain.entities.InsuranceScore
import dev.rudge.domain.entities.MaritalStatus
import dev.rudge.domain.entities.UserInformation

class LifeRiskScoreService : RiskScoreService {
    override fun calculate(userInformation: UserInformation) = when {
        userInformation.age > 60 -> InsuranceScore.INELIGIBLE
        else -> {
            var score = userInformation.getBaseScore()
            score = calculateScoreByAge(userInformation, score)
            score = calculateScoreByIncome(userInformation, score)
            score = calculateScoreByDependents(userInformation, score)
            score = calculateScoreByMaritalStatus(userInformation, score)
            InsuranceScore.getByScore(score)
        }
    }

    private fun calculateScoreByMaritalStatus(
        userInformation: UserInformation,
        score: Int
    ) = takeIf { userInformation.maritalStatus == MaritalStatus.MARRIED }?.let {
        score + 1
    } ?: score
}