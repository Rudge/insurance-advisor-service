package dev.rudge.domain.entities

import com.fasterxml.jackson.annotation.JsonValue

enum class MaritalStatus(@JsonValue val desc: String) {
    SINGLE("single"),
    MARRIED("married"),
}