package com.example.aplicacionmarzo.domain.usecase

import com.example.aplicacionmarzo.domain.models.UpdateRestaurante
import com.example.aplicacionmarzo.domain.repository.RestauranteRepository
import javax.inject.Inject

class UpdateRestauranteUseCase @Inject constructor(
    private val repository: RestauranteRepository
) {
    suspend operator fun invoke(id: Int, updateData: UpdateRestaurante) {
        repository.actualizarRestaurante(id, updateData)
    }
}
