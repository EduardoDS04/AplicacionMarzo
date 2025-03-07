package com.example.aplicacionmarzo.ui.views.activities

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.aplicacionmarzo.R
import com.example.aplicacionmarzo.databinding.ActivityMainBinding
import com.example.aplicacionmarzo.ui.dialogs.DialogRestaurante
import com.example.aplicacionmarzo.ui.viewmodel.RestauranteViewModel
import com.example.aplicacionmarzo.ui.views.fragments.FragmentPpal
import com.example.aplicacionmarzo.utils.JwtManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val restauranteViewModel: RestauranteViewModel by viewModels()
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var jwtManager: JwtManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        jwtManager = JwtManager(this)

        // Verificar autenticación
        if (!jwtManager.isLoggedIn()) {
            redirectToLogin()
            return
        }

        setupNavigation()
        setupUIComponents()

        // Observar errores
        restauranteViewModel.error.observe(this) { errorMsg ->
            if (errorMsg.isNotEmpty()) {
                Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show()
                Log.e("MainActivity", "Error del ViewModel: $errorMsg")
            }
        }
    }

    private fun setupNavigation() {
        setSupportActionBar(binding.toolbar)
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.fragmentConf,
                R.id.fragmentPpal,
                R.id.fragmentComments
            ),
            binding.drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)
        binding.navView.itemIconTintList = null

        // Navegar al fragmento principal inicialmente
        navController.navigate(R.id.fragmentPpal)

        // Listener para manejar navegación
        binding.navView.setNavigationItemSelectedListener { item ->
            handleNavigationSelection(item)
            true
        }
    }

    private fun setupUIComponents() {
        updateNavHeader()

        binding.fabAddRestaurante.setOnClickListener {
            DialogRestaurante(
                restaurante = null,
                onAction = { nuevoRestaurante ->
                    Log.d("MainActivity", "Añadiendo restaurante: ${nuevoRestaurante.nombre}")
                    restauranteViewModel.agregarRestaurante(nuevoRestaurante)

                    // Retardo breve para asegurar que la petición se complete
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(500)

                        // Forzar actualización de lista
                        getCurrentFragment()?.let { fragment ->
                            if (fragment is FragmentPpal) {
                                fragment.refreshData()
                                Toast.makeText(this@MainActivity, "Restaurante añadido correctamente", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            ).show(supportFragmentManager, "DialogAddRestaurante")
        }
    }

    private fun getCurrentFragment(): Fragment? {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as? NavHostFragment
        return navHostFragment?.childFragmentManager?.fragments?.firstOrNull()
    }

    private fun handleNavigationSelection(item: MenuItem) {
        when (item.itemId) {
            R.id.nav_logout -> logoutUser()
            else -> navController.navigate(item.itemId)
        }
        binding.drawerLayout.closeDrawers()
    }

    private fun updateNavHeader() {
        val headerView = binding.navView.getHeaderView(0)

        // Actualizar el nombre del usuario
        val userNameTextView: TextView = headerView.findViewById(R.id.textViewUserName)
        userNameTextView.text = jwtManager.getUserName() ?: "Eduardo"

        // Actualizar el email del usuario
        val userEmailTextView: TextView = headerView.findViewById(R.id.textViewUserEmail)
        userEmailTextView.text = jwtManager.getUserEmail() ?: "edirnoyin@gmail.com"
    }

    private fun logoutUser() {
        jwtManager.clearToken()
        redirectToLogin()
    }

    private fun redirectToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar, menu)
        // Hacer visibles los íconos del menú
        if (menu.javaClass.simpleName == "MenuBuilder") {
            try {
                val field = menu.javaClass.getDeclaredField("mOptionalIconsVisible")
                field.isAccessible = true
                field.setBoolean(menu, true)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                logoutUser()
                true
            }
            R.id.action_search -> {
                mostrarDialogoBusqueda()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun mostrarDialogoBusqueda() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Buscar por Precio por debajo de")

        // Campo de entrada para ingresar el precio
        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
        builder.setView(input)

        builder.setPositiveButton("Buscar") { _, _ ->
            val precioIngresado = input.text.toString().toDoubleOrNull()
            if (precioIngresado != null) {
                // Encuentra el fragmento actual y aplica el filtro si es FragmentPpal
                val currentFragment = getCurrentFragment()

                if (currentFragment is FragmentPpal) {
                    currentFragment.filtrarPorPrecio(precioIngresado)
                    Toast.makeText(this, "Mostrando restaurantes con precio <= $precioIngresado €", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Ingrese un precio válido", Toast.LENGTH_SHORT).show()
            }
        }

        // Botón para mostrar todos
        builder.setNeutralButton("Mostrar todos") { _, _ ->
            val currentFragment = getCurrentFragment()

            if (currentFragment is FragmentPpal) {
                currentFragment.mostrarTodos()
                Toast.makeText(this, "Mostrando todos los restaurantes", Toast.LENGTH_SHORT).show()
            }
        }

        // Botón de "Cancelar"
        builder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.dismiss()
        }

        builder.show()
    }
}