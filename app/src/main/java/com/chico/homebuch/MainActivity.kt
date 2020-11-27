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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private val movingDao by lazy { AppDataBase.getInstance(application)?.getMoneyDao() }
    private lateinit var moneyAdapter: MoneyAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setActivityFlags()
        setActivityOrientation()
        initRecycler()
        updateRecycler()


        val addNewMoneyMovingButton = findViewById<Button>(R.id.addNewMovingMoney_button)
        addNewMoneyMovingButton.setOnClickListener {
            startActivityForResult(
                Intent(this, AddNewmoneyMovingActivity::class.java),
                1010
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

    private fun initRecycler() {
        moneyAdapter = MoneyAdapter()
        recyclerView = findViewById(R.id.recycler_view)

        recyclerView.apply {
            adapter = moneyAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

    private fun updateRecycler() {
        CoroutineScope(Dispatchers.IO).launch {
            val moneyList = movingDao?.getMovingMoneyInfo()
            withContext(Dispatchers.Main) {
                if (moneyList != null) {
                    moneyAdapter.updateList(moneyList)
                }
            }
        }
    }

    private fun updateRecyclerWithCondition(view: Int){
        CoroutineScope(Dispatchers.IO).launch {
            val moneyList = movingDao?.getMovingMoneyInfoIncomeOrCosts(view)
            withContext(Dispatchers.Main) {
                if (moneyList != null) {
                    moneyAdapter.updateList(moneyList)
                }
            }
        }
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.incomingMoney_button -> {updateRecyclerWithCondition(1)}
            R.id.costMoney_button -> {updateRecyclerWithCondition(0)}
            R.id.allMovingMoney_button -> {updateRecycler()}
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1010 && resultCode == Activity.RESULT_OK){
            updateRecycler()
        }
    }
}