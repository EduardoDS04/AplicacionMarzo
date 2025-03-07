package com.example.aplicacionmarzo.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Usuario(
    val id: Int? = null,
    val dni: String,
    val nombre: String,
    val email: String,
    val password: String,
    val token: String = "" // Token JWT del usuario, con valor por defecto vacío
)
