package com.luizeduardobrandao.hiltnotas

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint

// * Activity principal que hospeda o NavHostFragment para gerenciar a navegação entre fragments.
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Depois de setContentView, o FragmentContainerView já existe no layout.
        val navHost = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        // Aqui sim temos um NavController garantido:
        val navController = navHost.navController

        // Agora registramos nosso listener sem risco de IllegalStateException:
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.welcomeFragment) {
                supportActionBar?.hide()
            } else {
                supportActionBar?.show()
            }
        }
    }
    override fun onSupportNavigateUp(): Boolean =
        supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment)
            .let { (it as NavHostFragment).navController }
            .navigateUp() || super.onSupportNavigateUp()
}