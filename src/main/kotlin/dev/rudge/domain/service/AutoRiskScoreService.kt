package dev.rudge.domain.service

import dev.rudge.domain.entities.InsuranceScore
import dev.rudge.domain.entities.UserInformation
import dev.rudge.domain.entities.Vehicle
import java.time.LocalDate

class AutoRiskScoreService : RiskScoreService {
    override fun calculate(userInformation: UserInformation) = when {
        userInformation.income == 0 -> InsuranceScore.INELIGIBLE
        userInformation.vehicle == null -> InsuranceScore.INELIGIBLE
        userInformation.house == null -> InsuranceScore.INELIGIBLE
        else -> {
            var score = userInformation.getBaseScore()
            score = calculateScoreByAge(userInformation, score)
            score = calculateScoreByIncome(userInformation, score)
            score = calculateScoreByVehicleYear(userInformation.vehicle, score)
            InsuranceScore.getByScore(score)
        }
    }

    private fun calculateScoreByVehicleYear(
        vehicle: Vehicle,
        score: Int
    ) = takeIf { LocalDate.now().year - vehicle.year <= 5 }?.let {
        score + 1
    } ?: score
}