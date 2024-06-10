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
import com.tirupati.vendor.databinding.LandingScreenSupervisorBinding
import com.tirupati.vendor.fragmnts.CustomerFragment
import com.tirupati.vendor.fragmnts.PoListFragment
import com.tirupati.vendor.helper.SessionManager
import com.tirupati.vendor.helper.interfaces.ToolbarTitleChangeListener
import com.tirupati.vendor.model.GateRESPONSEDATA
import com.tirupati.vendor.model.ResponseData
import com.tirupati.vendor.network.NetworkState
import com.tirupati.vendor.viewmodels.GatekeeperListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LandingScreenCustomerActivity : AppCompatActivity(), ToolbarTitleChangeListener {

    @Inject
    lateinit var sessionManager: SessionManager
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toolbar: Toolbar
    private val gateKeeperVm: GatekeeperListViewModel by viewModels()


    lateinit var binding: LandingScreenSupervisorBinding

    companion object{
         var toolbarS: Toolbar?  =null
        var listOfAllCompanySuperviser: ArrayList<GateRESPONSEDATA> = arrayListOf()


        fun changeTitle(title:String){
            if(toolbarS!=null){
                val titleTextVw = toolbarS!!.findViewById<TextView>(R.id.toolbarTitle)
                titleTextVw.setText(title)


            }
        }

        fun changeIcon(){
            if(toolbarS!=null){
                toolbarS!!.setNavigationIcon(R.drawable.menu)


            }
        }

        fun showIcon(changed:Boolean=false){

            if(toolbarS!=null){
                val img = toolbarS!!.findViewById<ImageView>(R.id.otp_back_iv)

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
        binding = LandingScreenSupervisorBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)
         toolbar = findViewById(R.id.main_toolbar)
        toolbarS=toolbar
        toolbar.title = title
        setSupportActionBar(toolbar)
        val reload:ImageView = findViewById<ImageView>(R.id.otp_back_iv)
        reload.setOnClickListener {
          //  callTheListApiForSupervisor()
        }

        drawerLayout = findViewById(R.id.drawer_acedamy)
        val navView: NavigationView = findViewById(R.id.navigationView)

        val navController = findNavController(R.id.host_fragment_appflow_customer)


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
            navController.navigate(R.id.vendorPageFragment)
          //  navController.navigate(R.id.customerFragment)
        }

        findViewById<TextView>(R.id.txtLogoutItem).setOnClickListener {
            closeDrawer()
            sessionManager.logout()
            startActivity(Intent(this, SIgnInRegisterActivity::class.java))

            finish()
        }
       /* findViewById<TextView>(R.id.txtManageNav).setOnClickListener {
            closeDrawer()
            navController.navigate(R.id.manageProfileFragment)
        }*/
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
        navController = findNavController(R.id.app_flow_supervisor)
        /* return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()*/

        return NavigationUI.navigateUp(
            navController, drawerLayout
        )
    }
    fun callTheListApiForSupervisor() {
        val header = HashMap<String, String>()
        header["Accept"] = "application/json"
        header["version"] = "1"
        header["Authorization"] = "${sessionManager.loginToken}"
        header["userID"]="${sessionManager.user?.RESPONSEDATA?.USER_ID}"
        lifecycleScope.launch {
            var response = gateKeeperVm.getSuperViserData(header)

            when (response) {

                is NetworkState.Success -> {
                    listOfAllCompanySuperviser.clear()

                    listOfAllCompanySuperviser = response.body.RESPONSEDATA


                    CustomerFragment.reloadUi()

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