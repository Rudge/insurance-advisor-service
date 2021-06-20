package dev.rudge.domain.service

import dev.rudge.domain.entities.House
import dev.rudge.domain.entities.InsuranceScore
import dev.rudge.domain.entities.OwnershipHouseStatus
import dev.rudge.domain.entities.UserInformationFactory
import kotlin.test.assertEquals
import org.junit.Test

internal class HomeRiskScoreServiceTest {
    @Test
    fun `given an user information with the income equals or less than 0 when calculate score should return score INELIGIBLE`() {
        val userInformation =
            UserInformationFactory.sample(income = 0)

        val riskEvaluation = HomeRiskScoreService().calculate(userInformation)

        assertEquals(InsuranceScore.INELIGIBLE, riskEvaluation)
    }

    @Test
    fun `given an user information without vehicle when calculate risk score should return score INELIGIBLE`() {
        val userInformation = UserInformationFactory.sample(vehicle = null)

        val riskEvaluation = HomeRiskScoreService().calculate(userInformation)

        assertEquals(InsuranceScore.INELIGIBLE, riskEvaluation)
    }

    @Test
    fun `given an user information without house when calculate risk score should return score INELIGIBLE`() {
        val userInformation = UserInformationFactory.sample(house = null)

        val riskEvaluation = HomeRiskScoreService().calculate(userInformation)

        assertEquals(InsuranceScore.INELIGIBLE, riskEvaluation)
    }

    @Test
    fun `given an user information with the age is under 30 years old and 3 risk answers true when calculate risk score should deduct 2 risk points and return score REGULAR`() {
        val userInformation = UserInformationFactory.sample(age = 29)

        val riskEvaluation = HomeRiskScoreService().calculate(userInformation)

        assertEquals(InsuranceScore.REGULAR, riskEvaluation)
    }

    @Test
    fun `given an user information with the age is between 30 and 40 years old and 3 risk answers true when calculate risk score should deduct 1 risk points and return score REGULAR`() {
        val userInformation = UserInformationFactory.sample(age = 31)

        val riskEvaluation = HomeRiskScoreService().calculate(userInformation)

        assertEquals(InsuranceScore.REGULAR, riskEvaluation)
    }

    @Test
    fun `given an user information with the income above 200K and 3 risk answers true when calculate risk score should deduct 1 risk points and return score REGULAR`() {
        val userInformation =
            UserInformationFactory.sample(income = 201_000)

        val riskEvaluation = HomeRiskScoreService().calculate(userInformation)

        assertEquals(InsuranceScore.REGULAR, riskEvaluation)
    }

    @Test
    fun `given an user information with the house mortgaged and 3 risk answers true when calculate risk score should add 1 risk points and return score REGULAR`() {
        val userInformation =
            UserInformationFactory.sample(house = House(OwnershipHouseStatus.MORTGAGED))

        val riskEvaluation = HomeRiskScoreService().calculate(userInformation)

        assertEquals(InsuranceScore.REGULAR, riskEvaluation)
    }
}