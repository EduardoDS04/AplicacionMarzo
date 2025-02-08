package com.example.aplicacionmarzo.domain.repository

import com.example.aplicacionmarzo.domain.models.Restaurante

interface RestauranteRepository {
    fun getRestaurantes(): List<Restaurante>
    fun agregarRestaurante(restaurante: Restaurante)
    fun eliminarRestaurante(posicion: Int)
    fun actualizarRestaurante(posicion: Int, restaurante: Restaurante)
}
