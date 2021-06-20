package dev.rudge.domain.entities

data class UserInformation(
    val age: Int,
    val dependents: Int,
    val house: House? = null,
    val income: Int,
    val maritalStatus: MaritalStatus,
    val riskQuestions: List<Boolean>,
    val vehicle: Vehicle? = null,
    private val baseScore: Int = riskQuestions.count { it }
) {
    fun getBaseScore() = baseScore
}
