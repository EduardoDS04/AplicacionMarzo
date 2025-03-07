package com.example.aplicacionmarzo.di

import android.content.Context
import android.widget.Toast
import retrofit2.HttpException
import java.net.SocketTimeoutException

object ErrorHandler {

    fun handleNetworkError(throwable: Throwable, context: Context) {
        val message = when (throwable) {
            is HttpException -> handleHttpError(throwable)
            is SocketTimeoutException -> "Timeout de conexión"
            else -> "Error desconocido: ${throwable.message}"
        }
        showToast(context, message)
    }

    private fun handleHttpError(exception: HttpException): String {
        return when (exception.code()) {
            401 -> "Sesión expirada, por favor inicia sesión de nuevo"
            404 -> "Recurso no encontrado"
            500 -> "Error interno del servidor"
            else -> "Error HTTP ${exception.code()}"
        }
    }

    private fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}