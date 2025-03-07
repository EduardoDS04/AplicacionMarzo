package com.example.aplicacionmarzo.domain.usecase

import com.example.aplicacionmarzo.domain.models.Restaurante
import com.example.aplicacionmarzo.domain.repository.RestauranteRepository
import javax.inject.Inject


class GetRestaurantesUseCase @Inject constructor(
    private val repository: RestauranteRepository
) {
    suspend operator fun invoke(): List<Restaurante> {
        return repository.getRestaurantes()
    }
}