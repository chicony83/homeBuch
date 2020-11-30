package com.chico.homebuch.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chico.homebuch.R
import com.chico.homebuch.adapter.MoneyAdapter
import com.chico.homebuch.constants.Const.TOTAL_MONEY_KEY
import com.chico.homebuch.database.AppDataBase
import com.chico.homebuch.utils.launchIO
import com.chico.homebuch.utils.launchUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ReportFragment : Fragment() {

    private val moneyDao by lazy { AppDataBase.getInstance(requireContext())?.getMoneyDao() }
    private val movingDao by lazy { AppDataBase.getInstance(requireContext())?.getMovingMoneyDao() }

    private lateinit var moneyAdapter: MoneyAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var totalMoneyTv: TextView
    private lateinit var fab: FloatingActionButton
//    private lateinit var bottomNav: BottomNavigationView
    private var total = 0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_report, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        startActivity()
        initRecycler()
        updateRecycler()

        with(requireActivity()) {
            fab = findViewById(R.id.addNewMovingMoney_button)
//            bottomNav = findViewById(R.id.bottom_navigation)
        }

//        bottomNav.setOnNavigationItemSelectedListener {menuItem->
//            when(menuItem.itemId){
//                R.id.income_money_menu_item -> {
//                    updateRecyclerWithCondition(1)
//                    true
//                }
//                R.id.costs_money_menu_item -> {
//                    updateRecyclerWithCondition(0)
//                    true
//                }
//                R.id.all_money_menu_item -> {
//                    updateRecycler()
//                    true
//                }
//                R.id.statistics_menu_item -> {
//                    updateRecycler()
//                    true
//                }
//                else -> false
//            }
//        }

        fab.setOnClickListener {
            val data = Bundle()
            data.putDouble(TOTAL_MONEY_KEY, total)
            findNavController().navigate(R.id.action_reportFragment_to_addMoneyMovingFragment, data)
        }
    }

    private fun initRecycler() {
        moneyAdapter = MoneyAdapter()
        recyclerView = requireActivity().findViewById(R.id.recycler_view)

        recyclerView.apply {
            adapter = moneyAdapter
            layoutManager = LinearLayoutManager(requireContext())
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

    private fun updateRecyclerWithCondition(view: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val moneyList = movingDao?.getMovingMoneyInfoIncomeOrCosts(view)
            withContext(Dispatchers.Main) {
                if (moneyList != null) {
                    moneyAdapter.updateList(moneyList)
                }
            }
        }
    }

    private fun startActivity() {
        totalMoneyTv = requireActivity().findViewById(R.id.total_money_tv)
        launchIO {
            total = moneyDao?.getMoney()?.myMoney ?: 0.0
            launchUI {
                totalMoneyTv.text = total.toString()
            }
        }
    }
}