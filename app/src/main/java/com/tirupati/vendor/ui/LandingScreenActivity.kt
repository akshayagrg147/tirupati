package com.tirupati.vendor.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.google.android.material.navigation.NavigationView
import com.tirupati.vendor.R
import com.tirupati.vendor.databinding.LandingScreenBinding
import com.tirupati.vendor.helper.SessionManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LandingScreenActivity : AppCompatActivity() {

    @Inject
    lateinit var sessionManager: SessionManager
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var drawerLayout: DrawerLayout
    lateinit var binding: LandingScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.landing_screen)
        binding = LandingScreenBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)
        val toolbar: Toolbar = findViewById(R.id.main_toolbar)
        toolbar.title = title
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer_acedamy)
        val navView: NavigationView = findViewById(R.id.navigationView)

        val navController = findNavController(R.id.host_fragment_appflow)


        NavigationUI.setupWithNavController(navView, navController)
        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)


        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.manageProfileFragment){
                supportActionBar?.setHomeAsUpIndicator(R.drawable.menu)
            }


        }

//        val toggle = ActionBarDrawerToggle(this, drawerLayout,null, R.string.navigation_drawer_open, R.string.navigation_drawer_close)

        toolbar.setNavigationIcon(R.drawable.menu)
        binding.sideOptions.txtManageNav.setOnClickListener {
            closeDrawer()
            toolbar.setNavigationIcon(R.drawable.menu)
            navController.navigate(R.id.vendorPageFragment)
        }
       /* findViewById<TextView>(R.id.txtManageNav).setOnClickListener {
            closeDrawer()
            navController.navigate(R.id.manageProfileFragment)
        }*/
    }

    override fun onSupportNavigateUp(): Boolean {
        navController = findNavController(R.id.host_fragment_appflow)
        /* return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()*/

        return NavigationUI.navigateUp(
            navController, drawerLayout
        )
    }


    @SuppressLint("RtlHardcoded")
    fun closeDrawer() {
        Handler(Looper.myLooper()!!).postDelayed({
            drawerLayout.apply { closeDrawer(Gravity.LEFT) }
            // drawerLayout.invalidate()
        }, 0)
    }

}