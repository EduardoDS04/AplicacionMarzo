package com.example.aplicacionmarzo.controller

import android.app.AlertDialog
import android.content.Context
import android.widget.Toast
import com.example.aplicacionmarzo.MainActivity
import com.example.aplicacionmarzo.adapter.AdapterRestaurante
import com.example.aplicacionmarzo.dao.DaoRestaurantes
import com.example.aplicacionmarzo.dialogues.DialogRestaurante
import com.example.aplicacionmarzo.models.Restaurante

class ControllerRestaurante(private val context: Context) {
    // Lista mutable que contiene los restaurantes, obtenida del DAO
    private val listaRestaurantes: MutableList<Restaurante> = DaoRestaurantes.myDao.getDataRestaurantes().toMutableList()
    // Adaptador para gestionar la vista de los restaurantes en el RecyclerView
    private lateinit var adapterRestaurante: AdapterRestaurante
    // Configura el adaptador para el RecyclerView de la actividad principal
    fun setAdapter() {
        val activity = context as MainActivity
        adapterRestaurante = AdapterRestaurante(
            listaRestaurantes,
            onDeleteClick = { posicion -> eliminarRestaurante(posicion) },
            onEditClick = { posicion -> editarRestaurante(posicion) }
        )
        activity.setRecyclerViewAdapter(adapterRestaurante)
    }
    // Método para eliminar un restaurante
    private fun eliminarRestaurante(posicion: Int) {
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
    // Método para agregar un nuevo restaurante
    fun addRestaurante() {
        DialogRestaurante(
            restaurante = null,
            onAction = { nuevoRestaurante ->
                listaRestaurantes.add(nuevoRestaurante)
                adapterRestaurante.notifyItemInserted(listaRestaurantes.size - 1)
            }
        ).show((context as MainActivity).supportFragmentManager, "DialogAddRestaurante")
    }
    // Método para editar un restaurante
    private fun editarRestaurante(posicion: Int) {
        val restaurante = listaRestaurantes[posicion]
        DialogRestaurante(
            restaurante = restaurante,
            onAction = { restauranteEditado ->
                listaRestaurantes[posicion] = restauranteEditado
                adapterRestaurante.notifyItemChanged(posicion)
            }
        ).show((context as MainActivity).supportFragmentManager, "DialogEditRestaurante")
    }
}
