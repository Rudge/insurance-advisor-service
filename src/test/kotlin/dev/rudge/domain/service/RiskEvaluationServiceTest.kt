package dev.rudge.domain.service

import dev.rudge.domain.entities.UserInformationFactory
import io.mockk.clearAllMocks
import io.mockk.mockk
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFailsWith

internal class RiskEvaluationServiceTest {

    private val autoRiskScoreService = mockk<AutoRiskScoreService>(relaxed = true)
    private val disabilityRiskScoreService = mockk<DisabilityRiskScoreService>(relaxed = true)
    private val homeRiskScoreService = mockk<HomeRiskScoreService>(relaxed = true)
    private val lifeRiskScoreService = mockk<LifeRiskScoreService>(relaxed = true)
    private val riskEvaluationService = RiskEvaluationService(
        autoRiskScoreService,
        disabilityRiskScoreService,
        homeRiskScoreService,
        lifeRiskScoreService
    )

    @BeforeTest
    fun before() {
        clearAllMocks()
    }

    @Test
    fun `given a personal information when calculate risk with age less than 0 should throws exception`() {
        val userInformation = UserInformationFactory.sample(age = -1)

        assertFailsWith(
            IllegalArgumentException::class,
            "Invalid age, must be equal or greater than 0!"
        ) {
            riskEvaluationService.calculate(userInformation)
        }
    }

    @Test
    fun `given a personal information when calculate risk with number of dependents less than 0 should throws exception`() {
        val userInformation =
            UserInformationFactory.sample(dependents = -1)

        assertFailsWith(
            IllegalArgumentException::class,
            "Invalid dependents, must be equal or greater than 0!"
        ) {
            riskEvaluationService.calculate(userInformation)
        }
    }

    @Test
    fun `given a personal information when calculate risk with income less than 0 should throws exception`() {
        val userInformation =
            UserInformationFactory.sample(income = -1)

        assertFailsWith(
            IllegalArgumentException::class,
            "Invalid income, must be equal or greater than 0!"
        ) {
            riskEvaluationService.calculate(userInformation)
        }
    }

    @Test
    fun `given a personal information when there are not 3 risk answers should throws exception`() {
        val userInformation = UserInformationFactory.sample()

        assertFailsWith(
            IllegalArgumentException::class,
            "Invalid risk answers, must have 3 answers!"
        ) {
            riskEvaluationService.calculate(userInformation)
        }
    }

}