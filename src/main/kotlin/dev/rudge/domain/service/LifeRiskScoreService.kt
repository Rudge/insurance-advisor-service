package dev.rudge.domain.service

import dev.rudge.domain.entities.InsuranceScore
import dev.rudge.domain.entities.MaritalStatus
import dev.rudge.domain.entities.UserInformation

class LifeRiskScoreService : RiskScoreService {
    override fun calculate(userInformation: UserInformation) = when {
        userInformation.age > 60 -> InsuranceScore.INELIGIBLE
        else -> {
            var score = userInformation.getBaseScore()
            if (userInformation.age < 30) {
                score -= 2
            } else if (userInformation.age in 30..40) {
                score--
            }
            if (userInformation.income > 200_000) {
                score--
            }
            if (userInformation.dependents > 0) {
                score++
            }
            if (userInformation.maritalStatus == MaritalStatus.MARRIED) {
                score++
            }
            InsuranceScore.getByScore(score)
        }
    }
}