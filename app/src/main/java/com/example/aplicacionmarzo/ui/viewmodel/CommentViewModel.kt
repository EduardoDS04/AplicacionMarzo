package com.example.aplicacionmarzo.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.aplicacionmarzo.model.Comment
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CommentViewModel @Inject constructor() : ViewModel() {

    private val _comments = MutableLiveData<List<Comment>?>()
    val comments: MutableLiveData<List<Comment>?> get() = _comments

    init {
        cargarComentarios()
    }

    private fun cargarComentarios() {
        _comments.value = listOf(
            Comment("Maria", "Este es un comentario de prueba", "12/01/2025"),
            Comment("Andrea", "Me encanta esta aplicación, es perfecta.", "13/01/2025"),
            Comment("Victoria", "Magnifica app.", "14/01/2025")
        )
    }

    fun agregarComentario(nuevoComentario: Comment) {
        val listaActualizada = _comments.value?.toMutableList() ?: mutableListOf()
        listaActualizada.add(nuevoComentario)
        _comments.value = listaActualizada
    }

    fun eliminarComentario(comentario: Comment) {
        val listaActualizada = _comments.value?.toMutableList()
        listaActualizada?.remove(comentario)
        _comments.value = listaActualizada
    }
}
