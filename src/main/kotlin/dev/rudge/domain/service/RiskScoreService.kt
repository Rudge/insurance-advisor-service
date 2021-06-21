package dev.rudge.domain.service

import dev.rudge.domain.entities.House
import dev.rudge.domain.entities.InsuranceScore
import dev.rudge.domain.entities.OwnershipHouseStatus
import dev.rudge.domain.entities.UserInformation

interface RiskScoreService {

    fun calculate(userInformation: UserInformation): InsuranceScore

    fun calculateScoreByAge(
        userInformation: UserInformation,
        score: Int
    ) = takeIf { userInformation.age < 30 }?.let {
        score - 2
    } ?: takeIf { userInformation.age in 30..40 }?.let {
        score - 1
    } ?: score

    fun calculateScoreByIncome(
        userInformation: UserInformation,
        score: Int
    ) = takeIf { userInformation.income > 200_000 }?.let {
        score - 1
    } ?: score

    fun calculateScoreByHouse(
        house: House,
        score: Int
    ) = takeIf { house.ownershipStatus == OwnershipHouseStatus.MORTGAGED }?.let {
        score + 1
    } ?: score

    fun calculateScoreByDependents(
        userInformation: UserInformation,
        score: Int
    ) = takeIf { userInformation.dependents > 0 }?.let {
        score + 1
    } ?: score

}