package com.chico.homebuch

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.navigation.NavController
import androidx.navigation.findNavController


import com.chico.homebuch.constants.Const.APP_PREF
import com.chico.homebuch.constants.Const.PREF_NAME

class MainActivity : AppCompatActivity() {

    private lateinit var nav: NavController

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setActivityFlags()

        val mySp = getSharedPreferences(APP_PREF, MODE_PRIVATE)
        nav = findNavController(R.id.fragment_container)


        if (mySp.getInt(PREF_NAME, 0) == 1) {
            nav.popBackStack(nav.currentDestination?.id ?: 0, true)
            nav.navigate(R.id.reportFragment)
        } else {
            nav.popBackStack(nav.currentDestination?.id ?: 0, true)
            nav.navigate(R.id.firstStartFragment)
        }
    }
    private fun setActivityFlags() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

    }

}