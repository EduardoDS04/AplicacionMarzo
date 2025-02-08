package com.example.aplicacionmarzo.domain.usecase

import com.example.aplicacionmarzo.domain.models.Restaurante
import com.example.aplicacionmarzo.domain.repository.RestauranteRepository
import javax.inject.Inject

class UpdateRestauranteUseCase @Inject constructor(
    private val restauranteRepository: RestauranteRepository
) {
    operator fun invoke(posicion: Int, restaurante: Restaurante) {
        restauranteRepository.actualizarRestaurante(posicion, restaurante)
    }
}
