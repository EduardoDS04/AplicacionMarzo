package com.example.aplicacionmarzo.controller

import android.app.AlertDialog
import android.content.Context
import android.widget.Toast
import com.example.aplicacionmarzo.adapter.AdapterRestaurante
import com.example.aplicacionmarzo.MainActivity
import com.example.aplicacionmarzo.dao.DaoRestaurantes
import com.example.aplicacionmarzo.models.Restaurante

class ControllerRestaurante(val context: Context) {
    lateinit var listaRestaurantes: MutableList<Restaurante>
    lateinit var adapterRestaurante: AdapterRestaurante

    init {
        initData()
    }

    private fun initData() {
        listaRestaurantes = DaoRestaurantes.myDao.getDataRestaurantes().toMutableList()
    }

    fun setAdapter() {
        val activity = context as MainActivity
        adapterRestaurante = AdapterRestaurante(
            listaRestaurantes
        ) { posicion -> eliminarRestaurante(posicion) }
        activity.setRecyclerViewAdapter(adapterRestaurante)
    }

    private fun eliminarRestaurante(posicion: Int) {
        if (posicion >= 0 && posicion < listaRestaurantes.size) {
            val restauranteEliminado = listaRestaurantes[posicion]

            AlertDialog.Builder(context)
                .setMessage("¿Deseas borrar el restaurante ${restauranteEliminado.nombre}?")
                .setPositiveButton("Sí") { _, _ ->
                    listaRestaurantes.removeAt(posicion)
                    adapterRestaurante.notifyItemRemoved(posicion)
                    Toast.makeText(context, "Restaurante eliminado: ${restauranteEliminado.nombre}", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
        }
    }
}
