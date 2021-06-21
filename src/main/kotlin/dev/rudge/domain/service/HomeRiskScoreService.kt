package dev.rudge.domain.service

import dev.rudge.domain.entities.InsuranceScore
import dev.rudge.domain.entities.UserInformation

class HomeRiskScoreService : RiskScoreService {
    override fun calculate(userInformation: UserInformation) = when {
        userInformation.income == 0 -> InsuranceScore.INELIGIBLE
        userInformation.vehicle == null -> InsuranceScore.INELIGIBLE
        userInformation.house == null -> InsuranceScore.INELIGIBLE
        else -> {
            var score = userInformation.getBaseScore()
            score = calculateScoreByAge(userInformation, score)
            score = calculateScoreByIncome(userInformation, score)
            score = calculateScoreByHouse(userInformation.house, score)
            InsuranceScore.getByScore(score)
        }
    }
}