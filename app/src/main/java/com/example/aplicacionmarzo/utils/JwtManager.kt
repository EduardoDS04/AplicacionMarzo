package com.example.aplicacionmarzo.utils

import android.content.Context
import android.content.SharedPreferences
import com.auth0.jwt.JWT

class JwtManager(private val context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        prefs.edit().putString("jwt_token", token).apply()
    }

    fun getToken(): String? = prefs.getString("jwt_token", null)

    fun clearToken() {
        prefs.edit().remove("jwt_token").apply()
    }

    fun getUserEmail(): String? {
        return getToken()?.let { token ->
            try {
                JWT.decode(token).getClaim("email").asString()
            } catch (e: Exception) {
                null
            }
        }
    }

    fun getUserName(): String? {
        return getToken()?.let { token ->
            try {
                JWT.decode(token).getClaim("name").asString()
            } catch (e: Exception) {
                null
            }
        }
    }

    fun isLoggedIn(): Boolean = getToken() != null
}