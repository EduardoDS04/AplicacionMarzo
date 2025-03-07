package com.example.aplicacionmarzo.domain.usecase

import com.example.aplicacionmarzo.domain.models.Restaurante
import com.example.aplicacionmarzo.domain.repository.RestauranteRepository
import javax.inject.Inject

class AddRestauranteUseCase @Inject constructor(
    private val restauranteRepository: RestauranteRepository
) {
    suspend operator fun invoke(restaurante: Restaurante) {
        restauranteRepository.agregarRestaurante(restaurante)
    }
}