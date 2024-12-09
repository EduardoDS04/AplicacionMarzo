package com.example.aplicacionmarzo.dialogues

import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.aplicacionmarzo.databinding.DialogRestauranteBinding
import com.example.aplicacionmarzo.models.Restaurante

class DialogRestaurante(
    private val restaurante: Restaurante? = null,
    private val onAction: (Restaurante) -> Unit
) : DialogFragment() {

    private var _binding: DialogRestauranteBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogRestauranteBinding.inflate(layoutInflater)

        val builder = AlertDialog.Builder(requireContext())
            .setTitle(if (restaurante == null) "Añadir Restaurante" else "Editar Restaurante")
            .setView(binding.root)
            .setPositiveButton("Guardar") { _, _ ->
                val nombre = binding.txtNombre.text.toString()
                val comida = binding.txtComida.text.toString()
                val tiempoEntrega = binding.txtTiempoEntrega.text.toString()
                val cantidad = binding.txtCantidad.text.toString().toIntOrNull()
                val precio = binding.txtPrecio.text.toString().toDoubleOrNull()
                val imagen = binding.txtImagen.text.toString()

                if (nombre.isNotBlank() && comida.isNotBlank() && tiempoEntrega.isNotBlank() && cantidad != null && precio != null) {
                    val nuevoRestaurante = Restaurante(
                        nombre = nombre,
                        comida = comida,
                        tiempoEntrega = tiempoEntrega,
                        cantidad = cantidad,
                        precio = precio,
                        imagen = imagen
                    )
                    onAction(nuevoRestaurante)
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Por favor, completa todos los campos correctamente.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .setNegativeButton("Cancelar") { dialog, _ -> dialog.dismiss() }

        restaurante?.let {
            binding.txtNombre.setText(it.nombre)
            binding.txtComida.setText(it.comida)
            binding.txtTiempoEntrega.setText(it.tiempoEntrega)
            binding.txtCantidad.setText(it.cantidad.toString())
            binding.txtPrecio.setText(it.precio.toString())
            binding.txtImagen.setText(it.imagen)
        }

        return builder.create()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
