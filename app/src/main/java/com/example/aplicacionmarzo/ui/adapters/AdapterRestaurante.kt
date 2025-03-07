package com.example.aplicacionmarzo.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.aplicacionmarzo.R
import com.example.aplicacionmarzo.domain.models.Restaurante

class AdapterRestaurante(
    private val listaRestaurantes: MutableList<Restaurante>,
    private val onDeleteClick: (Restaurante) -> Unit,
    private val onEditClick: (Restaurante) -> Unit
) : RecyclerView.Adapter<ViewHRestaurante>() {

    fun updateList(nuevaLista: List<Restaurante>) {
        listaRestaurantes.clear()
        listaRestaurantes.addAll(nuevaLista)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHRestaurante {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_restaurante, parent, false)
        return ViewHRestaurante(view, onEditClick, onDeleteClick)
    }


    override fun onBindViewHolder(holder: ViewHRestaurante, position: Int) {
        val restaurante = listaRestaurantes[position]
        holder.renderizar(restaurante)
    }

    override fun getItemCount(): Int = listaRestaurantes.size
}