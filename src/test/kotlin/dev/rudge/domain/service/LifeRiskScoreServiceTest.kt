package dev.rudge.domain.service

import dev.rudge.domain.entities.InsuranceScore
import dev.rudge.domain.entities.MaritalStatus
import dev.rudge.domain.entities.UserInformationFactory
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory

internal class LifeRiskScoreServiceTest {

    @Test
    fun `given an user information with the age over 60 years old when calculate risk score should return score INELIGIBLE`() {
        val userInformation = UserInformationFactory.sample(age = 61)

        val riskEvaluation = LifeRiskScoreService().calculate(userInformation)

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

                val riskEvaluation = LifeRiskScoreService().calculate(userInformation)

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

                val riskEvaluation = LifeRiskScoreService().calculate(userInformation)

                assertEquals(score, riskEvaluation)
            }
        }

    @TestFactory
    fun `given an user information with the income above 200K and risk answers when calculate risk score should deduct 1 risk points and return score expected`() =
        listOf(
            Triple(201_000, listOf(false, false, false), InsuranceScore.REGULAR),
            Triple(231_000, listOf(false, false, true), InsuranceScore.REGULAR),
            Triple(300_000, listOf(true, false, true), InsuranceScore.REGULAR),
            Triple(500_000, listOf(true, true, true), InsuranceScore.REGULAR),
        ).map { (income, answers, score) ->
            dynamicTest("given an user information with the income $income years old and risk answers $answers when calculate risk score should return $score") {
                val userInformation =
                    UserInformationFactory.sample(income = 201_000)

                val riskEvaluation = LifeRiskScoreService().calculate(userInformation)

                assertEquals(score, riskEvaluation)
            }
        }

    @TestFactory
    fun `given an user information with dependents and risk answers when calculate risk score should deduct 1 risk points and return score expected`() =
        listOf(
            Triple(1, listOf(false, false, false), InsuranceScore.REGULAR),
            Triple(2, listOf(false, false, true), InsuranceScore.REGULAR),
            Triple(3, listOf(true, false, true), InsuranceScore.RESPONSIBLE),
            Triple(4, listOf(true, true, true), InsuranceScore.RESPONSIBLE),
        ).map { (dependents, answers, score) ->
            dynamicTest("given an user information with $dependents dependents and risk answers $answers when calculate risk score should return $score") {
                val userInformation =
                    UserInformationFactory.sample(dependents = dependents, riskQuestions = answers)

                val riskEvaluation = LifeRiskScoreService().calculate(userInformation)

                assertEquals(score, riskEvaluation)
            }
        }

    @TestFactory
    fun `given an user information with married status and risk answers when calculate risk score should deduct 1 risk points and return score expected`() =
        listOf(
            listOf(false, false, false) to InsuranceScore.REGULAR,
            listOf(false, false, true) to InsuranceScore.REGULAR,
            listOf(true, false, true) to InsuranceScore.RESPONSIBLE,
            listOf(true, true, true) to InsuranceScore.RESPONSIBLE
        ).map { (answers, score) ->
            dynamicTest("given an user information with married status and risk answers $answers when calculate risk score should return $score") {
                val userInformation =
                    UserInformationFactory.sample(
                        maritalStatus = MaritalStatus.MARRIED,
                        riskQuestions = answers
                    )

                val riskEvaluation = LifeRiskScoreService().calculate(userInformation)

                assertEquals(score, riskEvaluation)
            }
        }
}