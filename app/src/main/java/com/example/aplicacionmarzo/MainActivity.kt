package com.example.aplicacionmarzo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aplicacionmarzo.adapter.AdapterRestaurante
import com.example.aplicacionmarzo.controller.ControllerRestaurante
import com.example.aplicacionmarzo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    // ViewBinding
    internal lateinit var binding: ActivityMainBinding
    // Controlador que maneja la lógica de la lista
    private lateinit var controllerRestaurante: ControllerRestaurante

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecyclerView()

        // Inicializamos el Controlador
        controllerRestaurante = ControllerRestaurante(this)
        controllerRestaurante.setAdapter()
    }

    private fun setupRecyclerView() {
        // Configuramos el RecyclerView con un LinearLayoutManager
        binding.myRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    // configuraramos el adaptador desde el controlador
    fun setRecyclerViewAdapter(adapter: AdapterRestaurante) {
        binding.myRecyclerView.adapter = adapter
    }
}
