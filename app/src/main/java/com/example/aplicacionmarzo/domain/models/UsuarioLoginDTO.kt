package com.example.aplicacionmarzo.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class UsuarioLoginDTO(
    val email: String,   // Email del usuario
    val password: String // Contraseña del usuario
)