package dev.rudge.domain.entities

import com.fasterxml.jackson.annotation.JsonValue

enum class OwnershipHouseStatus(@JsonValue val desc: String) {
    OWNED("owned"),
    MORTGAGED("mortgaged")
}