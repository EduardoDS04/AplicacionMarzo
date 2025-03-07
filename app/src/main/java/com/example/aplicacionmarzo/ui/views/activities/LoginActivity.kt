package com.example.aplicacionmarzo.ui.views.activities

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.aplicacionmarzo.databinding.ActivityLoginBinding
import com.example.aplicacionmarzo.ui.viewmodel.AuthViewModel
import com.example.aplicacionmarzo.utils.JwtManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val authViewModel: AuthViewModel by viewModels()
    private lateinit var jwtManager: JwtManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        jwtManager = JwtManager(this)

        if (jwtManager.isLoggedIn()) navigateToMain()
        setupUI()
        observeAuthState()
    }

    private fun observeAuthState() {
        lifecycleScope.launch {
            authViewModel.authState.collect { state ->
                when (state) {
                    is AuthViewModel.AuthState.Success -> {
                        Toast.makeText(this@LoginActivity, "Login exitoso", Toast.LENGTH_LONG).show()
                        navigateToMain()
                    }
                    is AuthViewModel.AuthState.Error -> {
                        Toast.makeText(this@LoginActivity, "Error en el login: ${state.message}", Toast.LENGTH_LONG).show()
                    }
                    is AuthViewModel.AuthState.Loading -> {
                    }
                    is AuthViewModel.AuthState.Idle -> {}
                }
            }
        }
    }

    private fun setupUI() {
        binding.buttonValidate.setOnClickListener {
            val credential = binding.editTextUser.text.toString()
            val password = binding.editTextPassword.text.toString()

            if (credential.isNotBlank() && password.isNotBlank()) {
                authViewModel.login(credential, password)
            } else {
                Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        binding.buttonCreate.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun navigateToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    // Oculta el teclado al tocar fuera de los EditText
    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val view = currentFocus
            if (view is EditText) {
                val outRect = Rect()
                view.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    view.clearFocus()
                    hideKeyboard(view)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }

    private fun hideKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}