package com.example.aplicacionmarzo.ui.adapters

import android.graphics.BitmapFactory
import android.util.Base64
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.aplicacionmarzo.databinding.ItemRestauranteBinding
import com.example.aplicacionmarzo.domain.models.Restaurante

class ViewHRestaurante(
    view: View,
    private val onEditClick: (Restaurante) -> Unit,
    private val onDeleteClick: (Restaurante) -> Unit
) : RecyclerView.ViewHolder(view) {

    private val binding = ItemRestauranteBinding.bind(view)

    fun renderizar(restaurante: Restaurante) {

        binding.btnEliminar.setOnClickListener { onDeleteClick(restaurante) }
        binding.btnEditar.setOnClickListener { onEditClick(restaurante) }

        binding.txtNombreRestaurante.text = restaurante.nombre
        binding.txtNombreComida.text = restaurante.comida
        binding.txtTiempoEntrega.text = "Entregado en ${restaurante.tiempoEntrega}"
        binding.txtCantidadPedido.text = "Cantidad: ${restaurante.cantidad}"
        binding.txtPrecio.text = "Precio: ${restaurante.precio} €"

        // Cargar imagen
        restaurante.imagen?.let { imagen ->
            if (imagen.startsWith("http")) {
                Glide.with(itemView.context)
                    .load(imagen)
                    .centerCrop()
                    .into(binding.imgRestaurante)
            } else {
                try {
                    val decodedBytes = Base64.decode(imagen, Base64.DEFAULT)
                    val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
                    binding.imgRestaurante.setImageBitmap(bitmap)
                } catch (e: Exception) {
                    binding.imgRestaurante.setImageResource(android.R.drawable.ic_menu_report_image)
                }
            }
        } ?: run {
            binding.imgRestaurante.setImageResource(android.R.drawable.ic_menu_report_image)
        }
    }
}