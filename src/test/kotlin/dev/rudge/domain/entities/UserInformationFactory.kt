package dev.rudge.domain.entities

object UserInformationFactory {

    fun sample(
        age: Int = 0,
        dependents: Int = 0,
        house: House? = House(OwnershipHouseStatus.OWNED),
        income: Int = 0,
        maritalStatus: MaritalStatus = MaritalStatus.SINGLE,
        riskQuestions: List<Boolean> = listOf(true, true, true),
        vehicle: Vehicle? = Vehicle(0),
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