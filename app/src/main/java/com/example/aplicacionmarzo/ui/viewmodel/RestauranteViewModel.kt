package com.example.aplicacionmarzo.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.aplicacionmarzo.domain.models.Restaurante
import com.example.aplicacionmarzo.domain.usecase.AddRestauranteUseCase
import com.example.aplicacionmarzo.domain.usecase.DeleteRestauranteUseCase
import com.example.aplicacionmarzo.domain.usecase.GetRestaurantesUseCase
import com.example.aplicacionmarzo.domain.usecase.UpdateRestauranteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RestauranteViewModel @Inject constructor(
    private val getRestaurantesUseCase: GetRestaurantesUseCase,
    private val addRestauranteUseCase: AddRestauranteUseCase,
    private val deleteRestauranteUseCase: DeleteRestauranteUseCase,
    private val updateRestauranteUseCase: UpdateRestauranteUseCase  // Asegúrate de inyectarlo
) : ViewModel() {

    private val _restaurantes = MutableLiveData<List<Restaurante>>()
    val restaurantes: LiveData<List<Restaurante>> get() = _restaurantes

    init {
        obtenerRestaurantes()
    }

    fun obtenerRestaurantes() {
        _restaurantes.value = getRestaurantesUseCase()
    }

    fun agregarRestaurante(nuevoRestaurante: Restaurante) {
        addRestauranteUseCase(nuevoRestaurante)
        obtenerRestaurantes()
    }

    fun eliminarRestaurante(posicion: Int) {
        deleteRestauranteUseCase(posicion)
        obtenerRestaurantes()
    }

    fun actualizarRestaurante(posicion: Int, restaurante: Restaurante) {
        updateRestauranteUseCase(posicion, restaurante)
        obtenerRestaurantes()
    }
}
