package dev.rudge.domain.entities

data class PersonalInformation(
    val age: Int,
    val dependents: Int,
    val house: House,
    val income: Int,
    val maritalStatus: String,
    val riskQuestions: List<Int>,
    val vehicle: Vehicle
)