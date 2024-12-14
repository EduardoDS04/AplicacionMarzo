package com.example.aplicacionmarzo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException

class RegisterActivity : AppCompatActivity() {

    // Declaración de vistas y FirebaseAuth
    private lateinit var btnRegister: Button
    private lateinit var btnGoToLogin: Button
    private lateinit var editEmail: EditText
    private lateinit var editPassword: EditText
    private lateinit var editConfirmPassword: EditText
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        init() // Inicializamos las vistas y FirebaseAuth
        start() // Configuramos los listeners
    }

    private fun init() {
        // Referencias a las vistas
        btnRegister = findViewById(R.id.buttonRegister)
        btnGoToLogin = findViewById(R.id.buttonGoToLogin)
        editEmail = findViewById(R.id.editTextEmail)
        editPassword = findViewById(R.id.editTextPassword)
        editConfirmPassword = findViewById(R.id.editTextConfirmPassword)

        // Inicializamos FirebaseAuth
        auth = FirebaseAuth.getInstance()
    }

    private fun start() {
        // Listener para el botón de registro
        btnRegister.setOnClickListener {
            val email = editEmail.text.toString().trim()
            val password = editPassword.text.toString().trim()
            val confirmPassword = editConfirmPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_LONG).show()
            } else if (password != confirmPassword) {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_LONG).show()
            } else {
                // Intentamos registrar al usuario
                registerUser(email, password) { result, msg ->
                    Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
                    if (result) {
                        goToLogin()
                    }
                }
            }
        }

        // Listener para el botón de volver al login
        btnGoToLogin.setOnClickListener {
            goToLogin()
        }
    }

    private fun registerUser(email: String, password: String, onResult: (Boolean, String) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Registro exitoso, enviamos correo de verificación
                    val user = auth.currentUser
                    user?.sendEmailVerification()
                        ?.addOnCompleteListener { verificationTask ->
                            if (verificationTask.isSuccessful) {
                                Log.i("RegisterActivity", "Correo de verificación enviado")
                                auth.signOut() // Deslogueamos al usuario
                                onResult(true, "Registro exitoso. Por favor, verifica tu correo.")
                            } else {
                                onResult(false, "Error al enviar el correo de verificación.")
                            }
                        }
                        ?.addOnFailureListener { exception ->
                            Log.e("RegisterActivity", "Error al enviar correo: ${exception.message}")
                            onResult(false, "No se pudo enviar el correo de verificación: ${exception.message}")
                        }
                } else {
                    // Manejo de errores de FirebaseAuth
                    try {
                        throw task.exception ?: Exception("Error desconocido")
                    } catch (e: FirebaseAuthUserCollisionException) {
                        onResult(false, "El email ya está registrado.")
                    } catch (e: FirebaseAuthWeakPasswordException) {
                        onResult(false, "La contraseña es demasiado débil.")
                    } catch (e: FirebaseAuthInvalidCredentialsException) {
                        onResult(false, "El email proporcionado no es válido.")
                    } catch (e: Exception) {
                        onResult(false, e.message.toString())
                    }
                }
            }
    }

    private fun goToLogin() {
        // Navegamos al LoginActivity
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
