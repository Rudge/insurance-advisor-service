package dev.rudge.domain.service

import dev.rudge.domain.entities.UserInformationFactory
import io.mockk.clearAllMocks
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

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

    @BeforeEach
    fun before() {
        clearAllMocks()
    }

    @Test
    fun `given an user information with age less than 0 when calculate risk should throws exception`() {
        val userInformation = UserInformationFactory.sample(age = -1)

        assertThrows<IllegalArgumentException>(
            "Invalid age, must be equal or greater than 0!"
        ) {
            riskEvaluationService.calculate(userInformation)
        }
    }

    @Test
    fun `given an user information with number of dependents less than 0 when calculate risk should throws exception`() {
        val userInformation =
            UserInformationFactory.sample(dependents = -1)

        assertThrows<IllegalArgumentException>(
            "Invalid dependents, must be equal or greater than 0!"
        ) {
            riskEvaluationService.calculate(userInformation)
        }
    }

    @Test
    fun `given an user information with income less than 0 when calculate risk should throws exception`() {
        val userInformation =
            UserInformationFactory.sample(income = -1)

        assertThrows<IllegalArgumentException>(
            "Invalid income, must be equal or greater than 0!"
        ) {
            riskEvaluationService.calculate(userInformation)
        }
    }

    @Test
    fun `given an user information without risk answers when calculate risk should throws exception`() {
        val userInformation = UserInformationFactory.sample(riskQuestions = emptyList())

        assertThrows<IllegalArgumentException>(
            "Invalid risk answers, must have 3 answers!"
        ) {
            riskEvaluationService.calculate(userInformation)
        }
    }

}