package com.example.aplicacionmarzo.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class UpdateRestaurante(
    val nombre: String? = null,
    val comida: String? = null,
    val tiempoEntrega: String? = null,
    val cantidad: Int? = null,
    val precio: Double? = null,
    val imagen: String? = null
)