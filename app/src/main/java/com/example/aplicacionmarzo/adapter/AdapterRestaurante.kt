package com.example.aplicacionmarzo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.aplicacionmarzo.R
import com.example.aplicacionmarzo.models.Restaurante

class AdapterRestaurante(
    private val listaRestaurantes: MutableList<Restaurante>,
    private val onDeleteClick: (Int) -> Unit,
    private val onEditClick: (Int) -> Unit,
) : RecyclerView.Adapter<ViewHRestaurante>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHRestaurante {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_restaurante, parent, false)
        return ViewHRestaurante(view, onEditClick, onDeleteClick)
    }

    override fun onBindViewHolder(holder: ViewHRestaurante, position: Int) {
        holder.renderizar(listaRestaurantes[position])
    }

    override fun getItemCount(): Int = listaRestaurantes.size
}
