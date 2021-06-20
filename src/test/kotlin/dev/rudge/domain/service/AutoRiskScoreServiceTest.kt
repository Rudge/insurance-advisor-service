package dev.rudge.domain.service

import dev.rudge.domain.entities.InsuranceScore
import dev.rudge.domain.entities.UserInformationFactory
import dev.rudge.domain.entities.Vehicle
import kotlin.test.assertEquals
import org.junit.Test

internal class AutoRiskScoreServiceTest {
    @Test
    fun `given an user information with the income equals or less than 0 when calculate score should return score INELIGIBLE`() {
        val userInformation =
            UserInformationFactory.sample(income = 0)

        val riskEvaluation = AutoRiskScoreService().calculate(userInformation)

        assertEquals(InsuranceScore.INELIGIBLE, riskEvaluation)
    }

    @Test
    fun `given an user information without vehicle when calculate risk score should return score INELIGIBLE`() {
        val userInformation = UserInformationFactory.sample(vehicle = null)

        val riskEvaluation = AutoRiskScoreService().calculate(userInformation)

        assertEquals(InsuranceScore.INELIGIBLE, riskEvaluation)
    }

    @Test
    fun `given an user information without house when calculate risk score should return score INELIGIBLE`() {
        val userInformation = UserInformationFactory.sample(house = null)

        val riskEvaluation = AutoRiskScoreService().calculate(userInformation)

        assertEquals(InsuranceScore.INELIGIBLE, riskEvaluation)
    }

    @Test
    fun `given an user information with the age is under 30 years old and 3 risk answers true when calculate risk score should deduct 2 risk points and return score REGULAR`() {
        val userInformation = UserInformationFactory.sample(age = 29)

        val riskEvaluation = AutoRiskScoreService().calculate(userInformation)

        assertEquals(InsuranceScore.REGULAR, riskEvaluation)
    }

    @Test
    fun `given an user information with the age is between 30 and 40 years old and 3 risk answers true when calculate risk score should deduct 1 risk points and return score REGULAR`() {
        val userInformation = UserInformationFactory.sample(age = 31)

        val riskEvaluation = AutoRiskScoreService().calculate(userInformation)

        assertEquals(InsuranceScore.REGULAR, riskEvaluation)
    }

    @Test
    fun `given an user information with the income above 200K and 3 risk answers true when calculate risk score should deduct 1 risk points and return score REGULAR`() {
        val userInformation =
            UserInformationFactory.sample(income = 201_000)

        val riskEvaluation = AutoRiskScoreService().calculate(userInformation)

        assertEquals(InsuranceScore.REGULAR, riskEvaluation)
    }

    @Test
    fun `given a personal information when the vehicle was produced in the last 5 years and all risk answers is true should add 1 risk points and return score RESPONSIBLE`() {
        val userInformation =
            UserInformationFactory.sample(vehicle = Vehicle(4))

        val riskEvaluation = AutoRiskScoreService().calculate(userInformation)

        assertEquals(InsuranceScore.RESPONSIBLE, riskEvaluation)
    }
}