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
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentConf : Fragment() {

    private var _binding: FragmentConfBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPreferences: SharedPreferences

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

        // Cargar datos almacenados
        cargarDatosUsuario()

        // Guardar cambios
        binding.btnGuardarDatos.setOnClickListener {
            guardarDatosUsuario()
        }
    }

    private fun cargarDatosUsuario() {
        // Obtener datos desde SharedPreferences
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

        // Validación básica
        if (nombre.isEmpty()) {
            binding.editTextNombre.error = "El nombre es obligatorio"
            return
        }

        if (telefono.isEmpty()) {
            binding.editTextTelefono.error = "El teléfono es obligatorio"
            return
        }

        if (pais.isEmpty()) {
            binding.editTextPais.error = "El país es obligatorio"
            return
        }

        // Guardar datos en SharedPreferences
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