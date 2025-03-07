package com.example.aplicacionmarzo.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplicacionmarzo.domain.models.Restaurante
import com.example.aplicacionmarzo.domain.models.UpdateRestaurante
import com.example.aplicacionmarzo.domain.usecase.AddRestauranteUseCase
import com.example.aplicacionmarzo.domain.usecase.DeleteRestauranteUseCase
import com.example.aplicacionmarzo.domain.usecase.GetRestaurantesUseCase
import com.example.aplicacionmarzo.domain.usecase.UpdateRestauranteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RestauranteViewModel @Inject constructor(
    private val getRestaurantesUseCase: GetRestaurantesUseCase,
    private val addRestauranteUseCase: AddRestauranteUseCase,
    private val deleteRestauranteUseCase: DeleteRestauranteUseCase,
    private val updateRestauranteUseCase: UpdateRestauranteUseCase
) : ViewModel() {

    private val _restaurantes = MutableLiveData<List<Restaurante>>()
    val restaurantes: LiveData<List<Restaurante>> get() = _restaurantes

    private val _precioMaximo = MutableLiveData<Double?>()
    val precioMaximo: LiveData<Double?> = _precioMaximo

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    // Evento para notificar que se debe actualizar la lista
    private val _actualizacionCompletada = MutableLiveData<Boolean>()
    val actualizacionCompletada: LiveData<Boolean> = _actualizacionCompletada

    init {
        actualizarLista()
    }

    fun setPrecioMaximo(precio: Double) {
        _precioMaximo.value = precio
    }

    fun agregarRestaurante(nuevoRestaurante: Restaurante) {
        viewModelScope.launch {
            try {
                Log.d("RestauranteViewModel", "Iniciando agregación de restaurante: ${nuevoRestaurante.nombre}")

                // 1. Añadir al servidor primero
                addRestauranteUseCase(nuevoRestaurante)

                Log.d("RestauranteViewModel", "Restaurante agregado con éxito")

                // 2. Después de éxito, actualizar la lista completa desde el servidor
                delay(300)
                actualizarLista()

                // 3. Notificar que la operación se completó
                _actualizacionCompletada.value = true

            } catch (e: Exception) {
                Log.e("RestauranteViewModel", "Error al agregar: ${e.message}", e)
                _error.value = "Error al agregar: ${e.message}"
            }
        }
    }

    fun eliminarRestaurante(restauranteId: Int) {
        viewModelScope.launch {
            try {
                Log.d("RestauranteViewModel", "Iniciando eliminación de restaurante ID: $restauranteId")

                // 1. Eliminar en el servidor
                deleteRestauranteUseCase(restauranteId)

                Log.d("RestauranteViewModel", "Restaurante eliminado con éxito")

                // 2. Actualizar la lista
                delay(300)
                actualizarLista()

                // 3. Notificar que la operación se completo
                _actualizacionCompletada.value = true

            } catch (e: Exception) {
                Log.e("RestauranteViewModel", "Error al eliminar: ${e.message}", e)
                _error.value = "Error al eliminar: ${e.message}"
            }
        }
    }

    fun actualizarRestaurante(id: Int, restaurante: Restaurante) {
        viewModelScope.launch {
            try {
                Log.d("RestauranteViewModel", "Iniciando actualización de restaurante ID: $id")

                val updateData = UpdateRestaurante(
                    nombre = restaurante.nombre,
                    comida = restaurante.comida,
                    tiempoEntrega = restaurante.tiempoEntrega,
                    cantidad = restaurante.cantidad,
                    precio = restaurante.precio,
                    imagen = restaurante.imagen
                )

                // Actualizar en el servidor
                updateRestauranteUseCase(id, updateData)

                Log.d("RestauranteViewModel", "Restaurante actualizado con éxito")

                // 2. Actualizar la lista
                delay(300)
                actualizarLista()

                // 3. Notificar que la operación se completó
                _actualizacionCompletada.value = true

            } catch (e: Exception) {
                Log.e("RestauranteViewModel", "Error al actualizar: ${e.message}", e)
                _error.value = "Error al actualizar: ${e.message}"
            }
        }
    }

    fun actualizarLista() {
        viewModelScope.launch {
            try {
                Log.d("RestauranteViewModel", "Actualizando lista de restaurantes...")

                val listaActualizada = getRestaurantesUseCase()

                Log.d("RestauranteViewModel", "Lista actualizada obtenida: ${listaActualizada.size} restaurantes")

                // Actualizar el LiveData con la nueva lista
                _restaurantes.value = listaActualizada

            } catch (e: Exception) {
                Log.e("RestauranteViewModel", "Error al cargar lista: ${e.message}", e)
                _error.value = "Error al cargar: ${e.message}"
            }
        }
    }
}