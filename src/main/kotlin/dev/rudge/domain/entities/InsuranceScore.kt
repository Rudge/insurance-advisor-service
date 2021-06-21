package dev.rudge.domain.entities

import com.fasterxml.jackson.annotation.JsonValue

enum class InsuranceScore(@JsonValue val desc: String) {
    INELIGIBLE("ineligible"),
    ECONOMIC("economic"),
    REGULAR("regular"),
    RESPONSIBLE("responsible");

    companion object {
        fun getByScore(score: Int) = when {
            score <= 0 -> ECONOMIC
            score in 1..2 -> REGULAR
            score >= 3 -> RESPONSIBLE
            else -> INELIGIBLE
        }
    }
}