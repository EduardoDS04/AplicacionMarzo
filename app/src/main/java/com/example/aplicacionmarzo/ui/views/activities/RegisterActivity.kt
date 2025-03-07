package com.example.aplicacionmarzo.ui.views.activities

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.aplicacionmarzo.databinding.ActivityRegisterBinding
import com.example.aplicacionmarzo.domain.models.Usuario
import com.example.aplicacionmarzo.ui.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupListeners()
    }

    private fun setupListeners() {
        binding.buttonRegister.setOnClickListener {
            val usuario = Usuario(
                dni = binding.editTextDni.text.toString(),
                nombre = binding.editTextNombre.text.toString(),
                email = binding.editTextEmail.text.toString(),
                password = binding.editTextPassword.text.toString()
            )

            if (validateInputs(usuario)) {
                // Usamos lifecycleScope.launch para llamar a la función suspend
                lifecycleScope.launch {
                    val success = authViewModel.register(usuario)
                    val message = if (success) "Registro exitoso" else "Error al registrar"
                    Toast.makeText(this@RegisterActivity, message, Toast.LENGTH_LONG).show()
                    if (success) finish()
                }
            }
        }
    }

    private fun validateInputs(usuario: Usuario): Boolean {
        return when {
            usuario.dni.isEmpty() -> {
                Toast.makeText(this, "DNI requerido", Toast.LENGTH_SHORT).show()
                false
            }
            usuario.dni.length != 9 -> {
                Toast.makeText(this, "DNI inválido", Toast.LENGTH_SHORT).show()
                false
            }
            usuario.nombre.isEmpty() -> {
                Toast.makeText(this, "Nombre requerido", Toast.LENGTH_SHORT).show()
                false
            }
            binding.editTextPassword.text.toString() != binding.editTextConfirmPassword.text.toString() -> {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                false
            }
            else -> true
        }
    }
}
