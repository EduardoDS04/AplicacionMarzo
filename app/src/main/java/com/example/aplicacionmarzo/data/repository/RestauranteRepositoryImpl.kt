package com.example.aplicacionmarzo.data.repository

import com.example.aplicacionmarzo.data.datasource.DaoRestaurantes
import com.example.aplicacionmarzo.domain.models.Restaurante
import com.example.aplicacionmarzo.domain.repository.RestauranteRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RestauranteRepositoryImpl @Inject constructor(
    private val daoRestaurantes: DaoRestaurantes
) : RestauranteRepository {

    override fun getRestaurantes(): List<Restaurante> {
        return daoRestaurantes.getRestaurantes()
    }

    override fun agregarRestaurante(restaurante: Restaurante) {
        daoRestaurantes.addRestaurante(restaurante)
    }

    override fun eliminarRestaurante(posicion: Int) {
        daoRestaurantes.removeRestaurante(posicion)
    }

    override fun actualizarRestaurante(posicion: Int, restaurante: Restaurante) {
        daoRestaurantes.updateRestaurante(posicion, restaurante)
    }
}
