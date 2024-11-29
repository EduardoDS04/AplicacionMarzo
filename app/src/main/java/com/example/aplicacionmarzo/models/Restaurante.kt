package com.example.aplicacionmarzo.models

data class Restaurante(
    val nombre: String,
    val comida: String,
    val tiempoEntrega: String,
    val cantidad: Int,
    val precio: Double,
    val imagen: String
) {
    override fun toString(): String {
        return "Restaurante(nombre='$nombre', comida='$comida', tiempoEntrega='$tiempoEntrega', cantidad=$cantidad, precio=$precio, imagen='$imagen')"
    }
}
