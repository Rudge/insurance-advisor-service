package dev.rudge.domain.service

import dev.rudge.domain.entities.InsuranceScore
import dev.rudge.domain.entities.UserInformation

class AutoRiskScoreService : RiskScoreService {
    override fun calculate(userInformation: UserInformation) = when {
        userInformation.income == 0 -> InsuranceScore.INELIGIBLE
        userInformation.vehicle == null -> InsuranceScore.INELIGIBLE
        userInformation.house == null -> InsuranceScore.INELIGIBLE
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
            if (userInformation.vehicle.year <= 5) {
                score++
            }
            InsuranceScore.getByScore(score)
        }
    }
}