package com.example.aplicacionmarzo.ui.views.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.aplicacionmarzo.databinding.FragmentConfBinding
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentConf : Fragment() {

    private var _binding: FragmentConfBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConfBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = requireActivity().getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        auth = FirebaseAuth.getInstance()

        // Cargar datos almacenados
        cargarDatosUsuario()

        // Guardar cambios
        binding.btnGuardarDatos.setOnClickListener {
            guardarDatosUsuario()
        }
    }

    private fun cargarDatosUsuario() {
        // Obtener el email desde Firebase
        val user = auth.currentUser
        binding.editTextEmail.setText(user?.email ?: "No disponible")

        // Obtener nombre, teléfono y país desde SharedPreferences
        val nombre = sharedPreferences.getString("nombreUsuario", "")
        val telefono = sharedPreferences.getString("telefonoUsuario", "")
        val pais = sharedPreferences.getString("paisUsuario", "")

        binding.editTextNombre.setText(nombre)
        binding.editTextTelefono.setText(telefono)
        binding.editTextPais.setText(pais)
    }

    private fun guardarDatosUsuario() {
        val nombre = binding.editTextNombre.text.toString().trim()
        val telefono = binding.editTextTelefono.text.toString().trim()
        val pais = binding.editTextPais.text.toString().trim()

        val editor = sharedPreferences.edit()
        editor.putString("nombreUsuario", nombre)
        editor.putString("telefonoUsuario", telefono)
        editor.putString("paisUsuario", pais)
        editor.apply()

        Toast.makeText(requireContext(), "Datos guardados correctamente", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
