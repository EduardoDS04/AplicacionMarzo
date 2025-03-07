package com.example.aplicacionmarzo.data.repository

import com.example.aplicacionmarzo.data.service.ApiService
import com.example.aplicacionmarzo.domain.models.Restaurante
import com.example.aplicacionmarzo.domain.models.UpdateRestaurante
import com.example.aplicacionmarzo.domain.repository.RestauranteRepository
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class RestauranteRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : RestauranteRepository {

    override suspend fun getRestaurantes(): List<Restaurante> {
        return try {
            val response = apiService.getRestaurantes()
            if (response.isSuccessful) {
                response.body() ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun agregarRestaurante(restaurante: Restaurante) {
        val response = apiService.addRestaurante(restaurante)
        if (!response.isSuccessful) {
            throw Exception("Error al agregar restaurante: ${response.code()}")
        }
    }

    override suspend fun eliminarRestaurante(id: Int) {
        val response = apiService.deleteRestaurante(id)
        if (!response.isSuccessful) {
            throw Exception("Error al eliminar: ${response.code()}")
        }
    }

    override suspend fun actualizarRestaurante(id: Int, updateData: UpdateRestaurante) {
        val response = apiService.updateRestaurante(id, updateData)
        if (!response.isSuccessful) {
            throw Exception("Error ${response.code()}: ${response.errorBody()?.string()}")
        }
    }
}