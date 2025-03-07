package com.example.aplicacionmarzo.domain.repository

import com.example.aplicacionmarzo.domain.models.Restaurante
import com.example.aplicacionmarzo.domain.models.UpdateRestaurante

interface RestauranteRepository {
    suspend fun getRestaurantes(): List<Restaurante>
    suspend fun agregarRestaurante(restaurante: Restaurante)
    suspend fun eliminarRestaurante(id: Int)
    suspend fun actualizarRestaurante(id: Int, updateData: UpdateRestaurante)
}