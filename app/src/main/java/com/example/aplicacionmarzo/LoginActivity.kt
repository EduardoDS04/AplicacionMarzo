package com.example.aplicacionmarzo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.graphics.Rect

class LoginActivity : AppCompatActivity() {

    // Constantes de usuario y contraseña
    private val MYUSER = "Eduardo"
    private val MYPASS = "DamAndroid"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Referenciamos los componentes del layout
        val editTextUser = findViewById<EditText>(R.id.editTextUser)
        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)
        val buttonValidate = findViewById<Button>(R.id.buttonValidate)

        // Configuramos el OnClickListener del botón
        buttonValidate.setOnClickListener {
            val user = editTextUser.text.toString()
            val pass = editTextPassword.text.toString()

            // Verificamos si los datos son correctos del usuario y contraseña
            if (user == MYUSER && pass == MYPASS) {
                // Iniciar MainActivity si la validación es correcta
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                // Mostrar mensaje de error si la contraseña o el usuario que ingresamos es incorrecto
                Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Para ocultar el teclado al pulsar fuera de los EditText
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
