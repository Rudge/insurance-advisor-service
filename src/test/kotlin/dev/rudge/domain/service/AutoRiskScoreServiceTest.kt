package dev.rudge.domain.service

import dev.rudge.domain.entities.InsuranceScore
import dev.rudge.domain.entities.UserInformationFactory
import dev.rudge.domain.entities.Vehicle
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory

class AutoRiskScoreServiceTest {
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

    @TestFactory
    fun `given an user information with the age is under 30 years old and risk answers when calculate risk score should deduct 2 risk points and return score expected`() =
        listOf(
            Triple(1, listOf(false, false, false), InsuranceScore.ECONOMIC),
            Triple(13, listOf(false, false, true), InsuranceScore.ECONOMIC),
            Triple(20, listOf(true, false, true), InsuranceScore.ECONOMIC),
            Triple(29, listOf(true, true, true), InsuranceScore.REGULAR),
        ).map { (age, answers, score) ->
            dynamicTest("given an user information with the age $age years old and risk answers $answers when calculate risk score should return $score") {

                val userInformation =
                    UserInformationFactory.sample(age = age, riskQuestions = answers)

                val riskEvaluation = AutoRiskScoreService().calculate(userInformation)

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
            dynamicTest("given an user information with the age $age years old and risk answers $answers when calculate risk score should return $score") {
                val userInformation =
                    UserInformationFactory.sample(age = age, riskQuestions = answers)

                val riskEvaluation = AutoRiskScoreService().calculate(userInformation)

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
            dynamicTest("given an user information with the income $income years old and risk answers $answers when calculate risk score should return $score") {
                val userInformation =
                    UserInformationFactory.sample(income = 201_000, riskQuestions = answers)

                val riskEvaluation = AutoRiskScoreService().calculate(userInformation)

                assertEquals(score, riskEvaluation)
            }
        }

    @TestFactory
    fun `given an user information with the vehicle was produced in the last 5 years and risk answers when calculate risk score should add 1 risk points and return score expected`() =
        listOf(
            Triple(2018, listOf(false, false, false), InsuranceScore.REGULAR),
            Triple(2019, listOf(false, false, true), InsuranceScore.REGULAR),
            Triple(2020, listOf(true, false, true), InsuranceScore.RESPONSIBLE),
            Triple(2021, listOf(true, true, true), InsuranceScore.RESPONSIBLE),
        ).map { (year, answers, score) ->
            dynamicTest("given an user information with the vehicle of $year and risk answers $answers when calculate risk score should return $score") {
                val userInformation =
                    UserInformationFactory.sample(vehicle = Vehicle(year), riskQuestions = answers)

                val riskEvaluation = AutoRiskScoreService().calculate(userInformation)

                assertEquals(score, riskEvaluation)
            }
        }

    @TestFactory
    fun `given an user information with the vehicle was produced more than last 5 years and risk answers when calculate risk score should not add 1 risk points and return score expected`() =
        listOf(
            Triple(1955, listOf(false, false, false), InsuranceScore.ECONOMIC),
            Triple(1964, listOf(false, false, true), InsuranceScore.REGULAR),
            Triple(1987, listOf(true, false, true), InsuranceScore.REGULAR),
            Triple(2011, listOf(true, true, true), InsuranceScore.RESPONSIBLE),
        ).map { (year, answers, score) ->
            dynamicTest("given an user information with the vehicle of $year and risk answers $answers when calculate risk score should return $score") {
                val userInformation =
                    UserInformationFactory.sample(vehicle = Vehicle(year), riskQuestions = answers)

                val riskEvaluation = AutoRiskScoreService().calculate(userInformation)

                assertEquals(score, riskEvaluation)
            }
        }
}