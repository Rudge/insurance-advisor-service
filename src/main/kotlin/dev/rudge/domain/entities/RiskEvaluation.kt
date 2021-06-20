package dev.rudge.domain.entities

data class RiskEvaluation(
    val auto: InsuranceScore,
    val disability: InsuranceScore,
    val home: InsuranceScore,
    val life: InsuranceScore
)