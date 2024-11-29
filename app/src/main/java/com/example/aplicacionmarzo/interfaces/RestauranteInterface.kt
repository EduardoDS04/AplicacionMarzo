package com.example.aplicacionmarzo.interfaces

import com.example.aplicacionmarzo.models.Restaurante

interface InterfaceDao {
    fun getDataRestaurantes(): List<Restaurante>
}
