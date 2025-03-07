package com.example.aplicacionmarzo.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Restaurante(
    val id: Int? = null,
    val nombre: String,
    val comida: String,
    val tiempoEntrega: String,
    val cantidad: Int,
    val precio: Double,
    val imagen: String? = null
)
