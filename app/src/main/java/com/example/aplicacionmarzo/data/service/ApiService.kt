package com.example.aplicacionmarzo.data.service

import com.example.aplicacionmarzo.domain.models.Restaurante
import com.example.aplicacionmarzo.domain.models.UpdateRestaurante
import com.example.aplicacionmarzo.domain.models.Usuario
import com.example.aplicacionmarzo.domain.models.UsuarioLoginDTO
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    // Obtener todos los restaurantes
    @GET("restaurantes")
    suspend fun getRestaurantes(): Response<List<Restaurante>>

    // Crear un nuevo restaurante
    @POST("restaurantes")
    suspend fun addRestaurante(@Body restaurante: Restaurante): Response<Unit>

    @PUT("restaurantes/{id}")
    suspend fun updateRestaurante(
        @Path("id") id: Int,
        @Body updateData: UpdateRestaurante
    ): Response<Unit>

    // Eliminar un restaurante
    @DELETE("restaurantes/{id}")
    suspend fun deleteRestaurante(@Path("id") id: Int): Response<Unit>

    // Login de usuario
    @POST("usuarios/login")
    suspend fun login(@Body usuarioLogin: UsuarioLoginDTO): Response<Map<String, String>>

    // Registro de nuevo usuario
    @POST("usuarios/register")
    suspend fun register(@Body usuario: Usuario): Response<Unit>

}