package com.example.aplicacionmarzo.dao

import com.example.aplicacionmarzo.Repository
import com.example.aplicacionmarzo.interfaces.InterfaceDao
import com.example.aplicacionmarzo.models.Restaurante

class DaoRestaurantes private constructor() : InterfaceDao {
    companion object {
        val myDao: DaoRestaurantes by lazy { DaoRestaurantes() }
    }
    override fun getDataRestaurantes(): List<Restaurante> = Repository.listaRestaurantes

}

