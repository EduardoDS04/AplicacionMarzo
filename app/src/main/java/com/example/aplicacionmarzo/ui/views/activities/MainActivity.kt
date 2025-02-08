package com.example.aplicacionmarzo.ui.views.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aplicacionmarzo.R
import com.example.aplicacionmarzo.databinding.ActivityMainBinding
import com.example.aplicacionmarzo.ui.adapters.AdapterRestaurante
import com.example.aplicacionmarzo.ui.dialogs.DialogEliminarRestaurante
import com.example.aplicacionmarzo.ui.dialogs.DialogRestaurante
import com.example.aplicacionmarzo.ui.viewmodel.RestauranteViewModel
import com.google.firebase.auth.FirebaseAuth
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val restauranteViewModel: RestauranteViewModel by viewModels()
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Firebase
        auth = FirebaseAuth.getInstance()

        // Configuración de la Toolbar y el Navigation Component
        setSupportActionBar(binding.toolbar)
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.fragmentPedido,
                R.id.fragmentConf,
                R.id.fragmentPpal,
                R.id.fragmentComments
            ),
            binding.drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)
        binding.navView.itemIconTintList = null

        // Configuración del menú lateral (Drawer)
        binding.navView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_logout -> {
                    logoutUser()
                    true
                }
                R.id.fragmentComments -> {
                    navController.navigate(R.id.fragmentComments)
                    binding.myRecyclerView.visibility = View.GONE
                    binding.drawerLayout.closeDrawers()
                    true
                }
                else -> {
                    navController.navigate(item.itemId)
                    binding.myRecyclerView.visibility = View.VISIBLE
                    binding.drawerLayout.closeDrawers()
                    true
                }
            }
        }

        updateNavHeader()
        setupRecyclerView()

        // Observamos el LiveData de restaurantes y configuramos el adapter
        restauranteViewModel.restaurantes.observe(this) { restaurantes ->
            binding.myRecyclerView.adapter = AdapterRestaurante(
                listaRestaurantes = restaurantes.toMutableList(),
                onDeleteClick = { posicion ->
                    showDeleteConfirmation(posicion)
                },
                onEditClick = { posicion ->
                    showEditDialog(posicion)
                }
            )
        }

        //al pulsar el boton se muestra el diálogo para añadir un nuevo restaurante
        binding.fabAddRestaurante.setOnClickListener {
            DialogRestaurante(
                restaurante = null,
                onAction = { nuevoRestaurante ->
                    restauranteViewModel.agregarRestaurante(nuevoRestaurante)
                }
            ).show(supportFragmentManager, "DialogAddRestaurante")
        }
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
            R.id.action_carro -> {
                true
            }
            R.id.action_search -> {
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupRecyclerView() {
        binding.myRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun updateNavHeader() {
        val headerView = binding.navView.getHeaderView(0)
        val userEmailTextView: TextView = headerView.findViewById(R.id.textViewUserEmail)
        val currentUser = auth.currentUser
        userEmailTextView.text = currentUser?.email ?: "Invitado"
    }

    private fun logoutUser() {
        val sharedPref = getSharedPreferences("LoginPreferences", Context.MODE_PRIVATE)
        sharedPref.edit().putBoolean("isLoggedIn", false).apply()
        auth.signOut()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        if (navController.currentDestination?.id == R.id.fragmentComments) {
            binding.myRecyclerView.visibility = View.GONE
        } else {
            binding.myRecyclerView.visibility = View.VISIBLE
        }
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    // Función para mostrar el diálogo de confirmación antes de borrar
    private fun showDeleteConfirmation(posicion: Int) {
        val restaurante = restauranteViewModel.restaurantes.value?.get(posicion)
        if (restaurante != null) {
            DialogEliminarRestaurante(
                nombreRestaurante = restaurante.nombre,
                onConfirmarEliminacion = {
                    restauranteViewModel.eliminarRestaurante(posicion)
                }
            ).show(supportFragmentManager, "DialogEliminarRestaurante")
        }
    }

    // Función para mostrar el formulario de edición
    private fun showEditDialog(posicion: Int) {
        val restaurante = restauranteViewModel.restaurantes.value?.get(posicion)
        if (restaurante != null) {
            DialogRestaurante(
                restaurante = restaurante,
                onAction = { restauranteEditado ->
                    restauranteViewModel.actualizarRestaurante(posicion, restauranteEditado)
                }
            ).show(supportFragmentManager, "DialogEditarRestaurante")
        }
    }
}
