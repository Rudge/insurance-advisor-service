package dev.rudge.domain.service

import dev.rudge.domain.entities.House
import dev.rudge.domain.entities.InsuranceScore
import dev.rudge.domain.entities.OwnershipHouseStatus
import dev.rudge.domain.entities.UserInformationFactory
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory

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

    @TestFactory
    fun `given an user information with the age is under 30 years old and risk answers when calculate risk score should deduct 2 risk points and return score expected`() =
        listOf(
            Triple(1, listOf(false, false, false), InsuranceScore.ECONOMIC),
            Triple(13, listOf(false, false, true), InsuranceScore.ECONOMIC),
            Triple(20, listOf(true, false, true), InsuranceScore.ECONOMIC),
            Triple(29, listOf(true, true, true), InsuranceScore.REGULAR),
        ).map { (age, answers, score) ->
            DynamicTest.dynamicTest("given an user information with the age $age years old and risk answers $answers when calculate risk score should return $score") {

                val userInformation =
                    UserInformationFactory.sample(age = age, riskQuestions = answers)

                val riskEvaluation = HomeRiskScoreService().calculate(userInformation)

                assertEquals(score, riskEvaluation)
            }
        }

    @TestFactory
    fun `given an user information with the age is between 30 and 40 years old and risk answers when calculate risk score should deduct 1 risk points and return score expected`() =
        listOf(
            Triple(30, listOf(false, false, false), InsuranceScore.ECONOMIC),
            Triple(33, listOf(false, false, true), InsuranceScore.ECONOMIC),
            Triple(37, listOf(true, false, true), InsuranceScore.REGULAR),
            Triple(40, listOf(true, true, true), InsuranceScore.REGULAR),
        ).map { (age, answers, score) ->
            DynamicTest.dynamicTest("given an user information with the age $age years old and risk answers $answers when calculate risk score should return $score") {
                val userInformation =
                    UserInformationFactory.sample(age = age, riskQuestions = answers)

                val riskEvaluation = HomeRiskScoreService().calculate(userInformation)

                assertEquals(score, riskEvaluation)
            }
        }

    @TestFactory
    fun `given an user information with the income above 200K and risk answers when calculate risk score should deduct 1 risk points and return score expected`() =
        listOf(
            Triple(201_000, listOf(false, false, false), InsuranceScore.ECONOMIC),
            Triple(231_000, listOf(false, false, true), InsuranceScore.ECONOMIC),
            Triple(300_000, listOf(true, false, true), InsuranceScore.REGULAR),
            Triple(500_000, listOf(true, true, true), InsuranceScore.REGULAR),
        ).map { (income, answers, score) ->
            DynamicTest.dynamicTest("given an user information with the income $income years old and risk answers $answers when calculate risk score should return $score") {
                val userInformation =
                    UserInformationFactory.sample(income = 201_000, riskQuestions = answers)

                val riskEvaluation = HomeRiskScoreService().calculate(userInformation)

                assertEquals(score, riskEvaluation)
            }
        }

    @TestFactory
    fun `given an user information with the house mortgaged and risk answers when calculate risk score should add 1 risk points and return score expected`() =
        listOf(
            listOf(false, false, false) to InsuranceScore.REGULAR,
            listOf(false, false, true) to InsuranceScore.REGULAR,
            listOf(true, false, true) to InsuranceScore.RESPONSIBLE,
            listOf(true, true, true) to InsuranceScore.RESPONSIBLE
        ).map { (answers, score) ->
            DynamicTest.dynamicTest("given an user information with the house mortgaged and risk answers $answers when calculate risk score should add 1 risk points and return $score") {
                val userInformation =
                    UserInformationFactory.sample(
                        house = House(OwnershipHouseStatus.MORTGAGED),
                        riskQuestions = answers
                    )

                val riskEvaluation = HomeRiskScoreService().calculate(userInformation)

                assertEquals(score, riskEvaluation)
            }
        }
}