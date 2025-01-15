package com.example.aplicacionmarzo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aplicacionmarzo.adapter.AdapterRestaurante
import com.example.aplicacionmarzo.controller.ControllerRestaurante
import com.example.aplicacionmarzo.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var controllerRestaurante: ControllerRestaurante
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        setSupportActionBar(binding.toolbar)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView)
                as NavHostFragment
        navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.fragmentPedido, R.id.fragmentConf, R.id.fragmentPpal, R.id.fragmentComments),
            binding.drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)
        binding.navView.itemIconTintList = null

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
        controllerRestaurante = ControllerRestaurante(this)
        controllerRestaurante.setAdapter()

        binding.fabAddRestaurante.setOnClickListener {
            controllerRestaurante.addRestaurante()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar, menu)

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

    fun setRecyclerViewAdapter(adapter: AdapterRestaurante) {
        binding.myRecyclerView.adapter = adapter
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

}
