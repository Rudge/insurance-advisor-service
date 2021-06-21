package dev.rudge.domain.entities

object UserInformationFactory {

    fun sample(
        age: Int = 41,
        dependents: Int = 0,
        house: House? = House(OwnershipHouseStatus.OWNED),
        income: Int = 1,
        maritalStatus: MaritalStatus = MaritalStatus.SINGLE,
        riskQuestions: List<Boolean> = listOf(true, true, true),
        vehicle: Vehicle? = Vehicle(6),
    ) = UserInformation(
        age = age,
        dependents = dependents,
        house = house,
        income = income,
        maritalStatus = maritalStatus,
        riskQuestions = riskQuestions,
        vehicle = vehicle
    )
}