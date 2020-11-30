package com.chico.homebuch

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chico.homebuch.adapter.MoneyAdapter
import com.chico.homebuch.database.AppDataBase
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val reportsButton = findViewById<Button>(R.id.reportsButton)

        reportsButton.setOnClickListener {
            startActivity(Intent(this, ReportsActivity::class.java))
        }

        val addNewMoneyMovingButton = findViewById<FloatingActionButton>(R.id.addNewMovingMoney_button)
        addNewMoneyMovingButton.setOnClickListener {
            startActivityForResult(
                Intent(this, AddNewmoneyMovingActivity::class.java),
                1010
            )
        }

    }
}