package com.example.aplicacionmarzo.domain.usecase

import com.example.aplicacionmarzo.domain.repository.RestauranteRepository
import javax.inject.Inject

class DeleteRestauranteUseCase @Inject constructor(
    private val restauranteRepository: RestauranteRepository
) {
    suspend operator fun invoke(id: Int) {
        restauranteRepository.eliminarRestaurante(id)
    }
}