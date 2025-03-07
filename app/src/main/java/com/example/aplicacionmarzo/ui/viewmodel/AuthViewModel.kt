package com.example.aplicacionmarzo.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplicacionmarzo.data.service.ApiService
import com.example.aplicacionmarzo.domain.models.Usuario
import com.example.aplicacionmarzo.domain.models.UsuarioLoginDTO
import com.example.aplicacionmarzo.utils.JwtManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val apiService: ApiService,
    private val jwtManager: JwtManager
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    sealed class AuthState {
        object Idle : AuthState()
        object Loading : AuthState()
        data class Success(val token: String) : AuthState()
        data class Error(val message: String) : AuthState()
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val response = apiService.login(UsuarioLoginDTO(email, password))
                if (response.isSuccessful) {
                    response.body()?.get("token")?.let { token ->
                        jwtManager.saveToken(token)
                        _authState.value = AuthState.Success(token)
                    } ?: run {
                        _authState.value = AuthState.Error("Token no encontrado en la respuesta")
                    }
                } else {
                    _authState.value = AuthState.Error("Error: ${response.code()}")
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error("Error de red: ${e.message}")
            }
        }
    }

    suspend fun register(usuario: Usuario): Boolean {
        return try {
            val response = apiService.register(usuario)
            if (response.isSuccessful) {
                true // Registro exitoso
            } else {
                false // Error en el servidor (ej: usuario ya existe)
            }
        } catch (e: Exception) {
            false // Error de red
        }
    }
}