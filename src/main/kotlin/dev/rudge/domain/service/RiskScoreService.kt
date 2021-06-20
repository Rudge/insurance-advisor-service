package dev.rudge.domain.service

import dev.rudge.domain.entities.InsuranceScore
import dev.rudge.domain.entities.UserInformation

interface RiskScoreService {

    fun calculate(userInformation: UserInformation): InsuranceScore
}