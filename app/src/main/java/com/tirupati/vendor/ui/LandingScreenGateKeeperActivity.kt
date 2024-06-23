package com.tirupati.vendor.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.google.android.material.navigation.NavigationView
import com.tirupati.vendor.R
import com.tirupati.vendor.databinding.LandingScreenGatekeeperBinding
import com.tirupati.vendor.fragmnts.GateEntryFragment
import com.tirupati.vendor.helper.SessionManager
import com.tirupati.vendor.helper.interfaces.ToolbarTitleChangeListener
import com.tirupati.vendor.model.VendorRESPONSEDATAX
import com.tirupati.vendor.network.NetworkState
import com.tirupati.vendor.viewmodels.GatekeeperListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LandingScreenGateKeeperActivity : AppCompatActivity(), ToolbarTitleChangeListener {

    @Inject
    lateinit var sessionManager: SessionManager
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toolbar: Toolbar
    private val gateKeeperVm: GatekeeperListViewModel by viewModels()


    lateinit var binding: LandingScreenGatekeeperBinding

    companion object{
         var toolbarC: Toolbar?  =null
        var listOfAllCompany: ArrayList<VendorRESPONSEDATAX> = arrayListOf()
        fun changeIcon(){
            if(toolbarC!=null){
                toolbarC!!.setNavigationIcon(R.drawable.menu)
//                val itemToolBarTitle = toolbarC!!.findViewById<TextView>(R.id.otp_back_iv)

            }
        }
        fun changeTitle(title:String){
            if(LandingScreenGateKeeperActivity.toolbarC !=null){
                val titleTextVw = LandingScreenGateKeeperActivity.toolbarC!!.findViewById<TextView>(R.id.toolbarTitle)
                titleTextVw.setText(title)


            }
        }

        fun showIcon(changed:Boolean=false){

            if(toolbarC!=null){
                val img = toolbarC!!.findViewById<ImageView>(R.id.otp_back_iv)

                if(changed){
                    img.visibility= View.VISIBLE
                }else{

                    img.visibility= View.GONE
                }
            }
        }


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.landing_screen)
        binding = LandingScreenGatekeeperBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)
         toolbar = findViewById(R.id.main_toolbar)
        toolbarC=toolbar
        toolbar.title = title
        setSupportActionBar(toolbar)
        val reload:ImageView = findViewById<ImageView>(R.id.otp_back_iv)
        reload.setOnClickListener {
          callTheListApi()
        }

        drawerLayout = findViewById(R.id.drawer_acedamy)
        val navView: NavigationView = findViewById(R.id.navigationView)
        var navController: NavController? = null

            navController = findNavController(R.id.host_fragment_appflow_gatekeeper)



        NavigationUI.setupWithNavController(navView, navController)
        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)


        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);

        navController.addOnDestinationChangedListener { _, destination, _ ->
//            if (destination.id == R.id.gateEntryFragment){
//                supportActionBar?.setHomeAsUpIndicator(R.drawable.menu)
//            }else{
//
//            }



        }

//        val toggle = ActionBarDrawerToggle(this, drawerLayout,null, R.string.navigation_drawer_open, R.string.navigation_drawer_close)

        toolbar.setNavigationIcon(R.drawable.menu)
        binding.sideOptions.txtManageNav.setOnClickListener {
            closeDrawer()
            toolbar.setNavigationIcon(R.drawable.menu)
            navController.navigate(R.id.gateEntryFragment)
        }
        findViewById<TextView>(R.id.txtLogoutItem).setOnClickListener {
            closeDrawer()
            sessionManager.logout()
            startActivity(Intent(this, SIgnInRegisterActivity::class.java))

            finish()
        }
    }

    /*private fun setDefaultToolbarIcon() {
        toolbar.setNavigationIcon(R.drawable.your_default_icon)
    }*/

    private fun setHamburgerIcon() {
        toolbar.setNavigationIcon(R.drawable.menu)
    }

    override fun updateToolbarTitle(title: String) {
        val toolbar = findViewById<Toolbar>(R.id.main_toolbar)
        toolbar.title = title
    }

    override fun onSupportNavigateUp(): Boolean {
        navController = findNavController(R.id.host_fragment_appflow_gatekeeper)
        /* return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()*/

        return NavigationUI.navigateUp(
            navController, drawerLayout
        )
    }
    fun callTheListApi() {
        lifecycleScope.launch {
            var response = gateKeeperVm.getCompanyList()

            when (response) {

                is NetworkState.Success -> {
                    listOfAllCompany.clear()

                    listOfAllCompany = response.body.RESPONSEDATA


                    GateEntryFragment.reloadUi()

                }

                is NetworkState.Error<*> -> {
                    // Toast.makeText(context,response.msg.toString(),Toast.LENGTH_SHORT).show()
                }

                is NetworkState.NetworkException -> {
                }

                is NetworkState.HttpErrors.InternalServerError -> {
                }

                is NetworkState.HttpErrors.ResourceNotFound -> {
                }

                else -> {
                }
            }


        }

    }

    @SuppressLint("RtlHardcoded")
    fun closeDrawer() {
        Handler(Looper.myLooper()!!).postDelayed({
            drawerLayout.apply { closeDrawer(Gravity.LEFT) }
            // drawerLayout.invalidate()
        }, 0)
    }

}