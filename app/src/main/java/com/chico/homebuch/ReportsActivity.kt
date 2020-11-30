package com.chico.homebuch

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chico.homebuch.adapter.MoneyAdapter
import com.chico.homebuch.database.AppDataBase
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ReportsActivity : AppCompatActivity() {

    private val movingDao by lazy { AppDataBase.getInstance(application)?.getMovingMoneyDao() }
    private lateinit var moneyAdapter: MoneyAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reports)
        initRecycler()
        updateRecycler()

        val addNewMoneyMovingButton = findViewById<FloatingActionButton>(R.id.addNewMovingMoney_button)
        addNewMoneyMovingButton.setOnClickListener {
            startActivityForResult(
                Intent(this, AddNewmoneyMovingActivity::class.java),
                1010
            )
        }

    }

    private fun initRecycler() {
        moneyAdapter = MoneyAdapter()
        recyclerView = findViewById(R.id.recycler_view)

        recyclerView.apply {
            adapter = moneyAdapter
            layoutManager = LinearLayoutManager(this@ReportsActivity)
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