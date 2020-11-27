package com.chico.homebuch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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

        CoroutineScope(Dispatchers.IO).launch {
            Log.e("TAG", movingDao?.getMovingMoneyInfo().toString())
        }
    }
}