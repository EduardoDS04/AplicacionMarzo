package com.example.aplicacionmarzo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aplicacionmarzo.adapter.AdapterRestaurante
import com.example.aplicacionmarzo.controller.ControllerRestaurante
import com.example.aplicacionmarzo.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var controllerRestaurante: ControllerRestaurante
    private lateinit var btnLogout: Button // Botón de Logout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializamos las vistas
        btnLogout = binding.btnLogout // Enlazamos el botón de logout
        setupRecyclerView()

        controllerRestaurante = ControllerRestaurante(this)
        controllerRestaurante.setAdapter()

        // Listener para el botón FAB
        binding.fabAddRestaurante.setOnClickListener {
            controllerRestaurante.addRestaurante()
        }

        // Listener para cerrar sesión
        btnLogout.setOnClickListener {
            logoutUser()
        }
    }

    private fun setupRecyclerView() {
        binding.myRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    // Configurar el adaptador desde el controlador
    fun setRecyclerViewAdapter(adapter: AdapterRestaurante) {
        binding.myRecyclerView.adapter = adapter
    }

    private fun logoutUser() {
        // Borramos la preferencia compartida de logueo
        val sharedPref = getSharedPreferences("LoginPreferences", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean("isLoggedIn", false)
        editor.apply()

        //  Cerramos la sesión de Firebase
        FirebaseAuth.getInstance().signOut()

        // Navegamos de vuelta al LoginActivity
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish() // Finalizamos la MainActivity
    }
}
