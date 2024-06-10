package com.tirupati.vendor.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import com.tirupati.vendor.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SIgnInRegisterActivity :  AppCompatActivity(){
    private lateinit var navController: NavController

override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
//        val navController = findNavController(R.id.main_nav_signin_flow)
    }
}