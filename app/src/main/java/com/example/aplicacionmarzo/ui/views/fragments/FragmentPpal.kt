package com.example.aplicacionmarzo.ui.views.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aplicacionmarzo.databinding.FragmentPpalBinding
import com.example.aplicacionmarzo.domain.models.Restaurante
import com.example.aplicacionmarzo.ui.adapters.AdapterRestaurante
import com.example.aplicacionmarzo.ui.dialogs.DialogEliminarRestaurante
import com.example.aplicacionmarzo.ui.dialogs.DialogRestaurante
import com.example.aplicacionmarzo.ui.viewmodel.RestauranteViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentPpal : Fragment() {

    private var _binding: FragmentPpalBinding? = null
    private val binding get() = _binding!!
    private val restauranteViewModel: RestauranteViewModel by viewModels()
    private var operacionEnCurso = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPpalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupObservers()
        refreshData() // Forzar actualización inicial
    }

    private fun setupRecyclerView() {
        binding.recyclerViewRestaurantes.layoutManager = LinearLayoutManager(requireContext())

        // Inicializar el adaptador con una lista vacía
        binding.recyclerViewRestaurantes.adapter = AdapterRestaurante(
            mutableListOf(),
            onDeleteClick = ::showDeleteConfirmation,
            onEditClick = ::showEditDialog
        )
    }

    private fun setupObservers() {
        // Observar la lista de restaurantes
        restauranteViewModel.restaurantes.observe(viewLifecycleOwner) { restaurantes ->
            Log.d("FragmentPpal", "Lista actualizada: ${restaurantes.size} restaurantes")
            updateRecyclerView(restaurantes)
            operacionEnCurso = false
        }

        // Observar errores
        restauranteViewModel.error.observe(viewLifecycleOwner) { errorMsg ->
            if (errorMsg.isNotEmpty()) {
                Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show()
                operacionEnCurso = false
            }
        }

        // Observar cuando se completan operaciones
        restauranteViewModel.actualizacionCompletada.observe(viewLifecycleOwner) { completado ->
            if (completado) {
                Log.d("FragmentPpal", "Operación completada, actualizando lista...")
                refreshData()
            }
        }
    }

    private fun updateRecyclerView(restaurantes: List<Restaurante>) {
        val adapter = binding.recyclerViewRestaurantes.adapter as AdapterRestaurante
        adapter.updateList(restaurantes)
    }

    // Función para aplicar filtro de precio desde MainActivity
    fun filtrarPorPrecio(precio: Double) {
        val listaCompleta = restauranteViewModel.restaurantes.value ?: return
        val filtrados = listaCompleta
            .filter { it.precio <= precio }
            .sortedBy { it.precio }
        updateRecyclerView(filtrados)
    }

    // Función para mostrar todos los restaurantes (quitar filtros)
    fun mostrarTodos() {
        refreshData()
    }

    private fun showDeleteConfirmation(restaurante: Restaurante) {
        // Evitar operaciones múltiples simultáneas
        if (operacionEnCurso) {
            Toast.makeText(context, "Hay una operación en curso", Toast.LENGTH_SHORT).show()
            return
        }

        DialogEliminarRestaurante(
            nombreRestaurante = restaurante.nombre,
            onConfirmarEliminacion = {
                restaurante.id?.let { id ->
                    operacionEnCurso = true
                    restauranteViewModel.eliminarRestaurante(id)
                    Toast.makeText(context, "Eliminando restaurante...", Toast.LENGTH_SHORT).show()
                }
            }
        ).show(requireActivity().supportFragmentManager, "DialogEliminarRestaurante")
    }

    private fun showEditDialog(restaurante: Restaurante) {
        // Evitar operaciones múltiples simultáneas
        if (operacionEnCurso) {
            Toast.makeText(context, "Hay una operación en curso", Toast.LENGTH_SHORT).show()
            return
        }

        DialogRestaurante(
            restaurante = restaurante,
            onAction = { actualizado ->
                restaurante.id?.let { id ->
                    operacionEnCurso = true
                    restauranteViewModel.actualizarRestaurante(id, actualizado)
                    Toast.makeText(context, "Actualizando restaurante...", Toast.LENGTH_SHORT).show()
                }
            }
        ).show(requireActivity().supportFragmentManager, "DialogEditarRestaurante")
    }

    override fun onResume() {
        super.onResume()
        // Actualizar datos cuando el fragmento se vuelve visible
        refreshData()
    }

    fun refreshData() {
        if (!operacionEnCurso) {
            Log.d("FragmentPpal", "Solicitando actualización de lista...")
            restauranteViewModel.actualizarLista()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}