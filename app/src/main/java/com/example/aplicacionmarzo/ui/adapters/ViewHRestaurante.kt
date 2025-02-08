package com.example.aplicacionmarzo.ui.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.aplicacionmarzo.databinding.ItemRestauranteBinding
import com.example.aplicacionmarzo.domain.models.Restaurante

class ViewHRestaurante(
    view: View,
    var onEditClick: (Int) -> Unit,
    var onDeleteClick: (Int) -> Unit
) : RecyclerView.ViewHolder(view) {

    private val binding = ItemRestauranteBinding.bind(view)

    init {
        // botón de eliminar
        binding.btnEliminar.setOnClickListener {
            onDeleteClick(adapterPosition)
        }
        // botón de editar
        binding.btnEditar.setOnClickListener {
            onEditClick(adapterPosition)
        }
    }

    fun renderizar(restaurante: Restaurante) {
        binding.txtNombreRestaurante.text = restaurante.nombre
        binding.txtNombreComida.text = restaurante.comida
        binding.txtTiempoEntrega.text = "Entregado en ${restaurante.tiempoEntrega}"
        binding.txtCantidadPedido.text = "Cantidad: ${restaurante.cantidad}"
        binding.txtPrecio.text = "Precio: ${restaurante.precio} €"
        // Cargar imagen con Glide
        Glide.with(itemView.context)
            .load(restaurante.imagen)
            .centerCrop()
            .into(binding.imgRestaurante)
    }
}
