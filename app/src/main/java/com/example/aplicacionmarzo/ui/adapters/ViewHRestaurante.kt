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
    var onEditClick: (Int) -> Unit,
    var onDeleteClick: (Int) -> Unit
) : RecyclerView.ViewHolder(view) {

    private val binding = ItemRestauranteBinding.bind(view)

    init {
        binding.btnEliminar.setOnClickListener { onDeleteClick(adapterPosition) }
        binding.btnEditar.setOnClickListener { onEditClick(adapterPosition) }
    }

    fun renderizar(restaurante: Restaurante) {
        binding.txtNombreRestaurante.text = restaurante.nombre
        binding.txtNombreComida.text = restaurante.comida
        binding.txtTiempoEntrega.text = "Entregado en ${restaurante.tiempoEntrega}"
        binding.txtCantidadPedido.text = "Cantidad: ${restaurante.cantidad}"
        binding.txtPrecio.text = "Precio: ${restaurante.precio} €"

        // Cargar imagen (URL o Base64)
        restaurante.imagen?.let { imagen ->
            if (imagen.startsWith("http")) {
                // Cargar desde URL con Glide
                Glide.with(itemView.context)
                    .load(imagen)
                    .centerCrop()
                    .into(binding.imgRestaurante)
            } else {
                // Cargar desde Base64
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
    } }