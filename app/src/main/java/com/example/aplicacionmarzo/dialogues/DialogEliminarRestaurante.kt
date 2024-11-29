package com.example.aplicacionmarzo.dialogues

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class DialogEliminarRestaurante(
    private val nombreRestaurante: String,
    private val onConfirmarEliminacion: () -> Unit
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle("Eliminar Restaurante")
            .setMessage("¿Estás seguro de que deseas eliminar \"$nombreRestaurante\"?")
            .setPositiveButton("Sí") { _, _ ->
                onConfirmarEliminacion()
            }
            .setNegativeButton("No", null)
            .create()
    }
}
