package com.example.aplicacionmarzo

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var btnLogin: Button
    private lateinit var btnCreate: Button
    private lateinit var editUser: EditText
    private lateinit var editPassword: EditText
    private lateinit var textViewForgotPassword: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Verificamos si el usuario ya está logueado
        val sharedPref = getSharedPreferences("LoginPreferences", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPref.getBoolean("isLoggedIn", false) // Por defecto es false

        if (isLoggedIn) {
            // Si el usuario está logueado, redirigimos a MainActivity
            startMainActivity()
            return
        }

        setContentView(R.layout.activity_login)

        init() // Inicializa los componentes de la vista
        start() // Configura los listeners
    }

    private fun init() {
        editUser = findViewById(R.id.editTextUser)
        editPassword = findViewById(R.id.editTextPassword)
        btnLogin = findViewById(R.id.buttonValidate)
        btnCreate = findViewById(R.id.buttonCreate)
        textViewForgotPassword = findViewById(R.id.textViewForgotPassword)

        auth = FirebaseAuth.getInstance() // Inicializa FirebaseAuth
    }

    private fun start() {
        // Listener para el botón de Login
        btnLogin.setOnClickListener {
            val email = editUser.text.toString().trim()
            val password = editPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_LONG).show()
            } else {
                loginUser(email, password) { success, message ->
                    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                    if (success) {
                        startMainActivity()
                    }
                }
            }
        }

        // Listener para el botón Create
        btnCreate.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        // Listener para recuperar contraseña
        textViewForgotPassword.setOnClickListener {
            val email = editUser.text.toString().trim()
            if (email.isNotEmpty()) {
                recoverPassword(email) { success, message ->
                    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                    if (!success) {
                        editUser.setText("") // Limpia el campo tras un error
                    }
                }
            } else {
                Toast.makeText(this, "Introduce tu correo para recuperar la contraseña", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun startMainActivity() {
        // Guardamos el estado de logueo en SharedPreferences
        val sharedPref = getSharedPreferences("LoginPreferences", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean("isLoggedIn", true) // Marcamos que el usuario está logueado
        editor.apply()

        // Navegamos a la pantalla principal
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // Finalizamos el LoginActivity
    }

    private fun loginUser(email: String, password: String, onResult: (Boolean, String) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user?.isEmailVerified == true) {
                        onResult(true, "Inicio de sesión exitoso.")
                    } else {
                        auth.signOut() // Desloguea al usuario si no ha verificado el correo
                        onResult(false, "Por favor, verifica tu correo antes de iniciar sesión.")
                    }
                } else {
                    // Manejo de errores de Firebase
                    var msg = ""
                    try {
                        throw task.exception ?: Exception("Error desconocido")
                    } catch (e: FirebaseAuthInvalidUserException) {
                        msg = "El usuario no existe o ha sido deshabilitado."
                    } catch (e: FirebaseAuthInvalidCredentialsException) {
                        msg = if (e.message?.contains("There is no user record corresponding to this identifier") == true) {
                            "El usuario no existe."
                        } else "Contraseña incorrecta."
                    } catch (e: Exception) {
                        msg = e.message.toString()
                    }
                    onResult(false, msg)
                }
            }
    }

    private fun recoverPassword(email: String, onResult: (Boolean, String) -> Unit) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onResult(true, "Correo de recuperación enviado. Revisa tu bandeja de entrada del correo.")
                } else {
                    var msg = ""
                    try {
                        throw task.exception ?: Exception("Error de reseteo inesperado")
                    } catch (e: FirebaseAuthInvalidCredentialsException) {
                        msg = "El formato del email es incorrecto."
                    } catch (e: Exception) {
                        msg = e.message.toString()
                    }
                    onResult(false, msg)
                }
            }
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
