package com.chico.homebuch

import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Button
import com.chico.homebuch.database.AppDataBase
import com.chico.homebuch.database.entity.MovingMoneyInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val movingDao by lazy { AppDataBase.getInstance(application)?.getMoneyDao() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setActivityFlags()
        setActivityOrientation()


        CoroutineScope(Dispatchers.IO).launch {
            Log.e("TAG", movingDao?.getMovingMoneyInfo().toString())
        }
        val addNewMoneyMovingButton = findViewById<Button>(R.id.addNewMovingMoney_button)

        addNewMoneyMovingButton.setOnClickListener {
            startActivity(
                Intent(this, AddNewmoneyMovingActivity::class.java)
            )
        }
    }

    private fun setActivityOrientation() {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    private fun setActivityFlags() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }

}