package dev.rudge.domain.service

import dev.rudge.domain.entities.PersonalInformation
import dev.rudge.domain.entities.RiskEvaluation

class RiskEvaluationService {
    fun calculate(personalInformation: PersonalInformation): RiskEvaluation {
        require(personalInformation.age >= 0) { "Invalid age, must be equal or greater than 0!" }
        require(personalInformation.dependents >= 0) { "Invalid dependents, must be equal or greater than 0!" }
        require(personalInformation.income >= 0) { "Invalid income, must be equal or greater than 0!" }
        require(personalInformation.age >= 0) { "Invalid age!" }
        return RiskEvaluation("", "", "", "")
    }
}