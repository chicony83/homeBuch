package com.chico.homebuch.ui

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.chico.homebuch.R
import com.chico.homebuch.constants.Const.MOVING_MONEY_KEY
import com.chico.homebuch.database.AppDataBase
import com.chico.homebuch.database.entity.Money
import com.chico.homebuch.database.entity.MovingMoneyInfo
import com.chico.homebuch.utils.launchIO
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class UpdateMoneyMovingFragment : Fragment() {

    private val movingMoneyDao by lazy {
        AppDataBase.getInstance(requireContext())?.getMovingMoneyDao()
    }
    private val moneyDao by lazy {
        AppDataBase.getInstance(requireContext())?.getMoneyDao()
    }

    private lateinit var incomeRb: RadioButton
    private lateinit var costsRb: RadioButton
    private lateinit var descriptionEt: EditText
    private lateinit var sumEt: EditText
    private lateinit var updateBtn: Button

    private var mMoney: MovingMoneyInfo? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_update_money_moving, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initLayout()
        setHasOptionsMenu(true)

        mMoney = requireArguments().getParcelable(MOVING_MONEY_KEY)
        mMoney?.let {
            showInfo(it)
        }
    }

    private fun showInfo(mMoney: MovingMoneyInfo) {
        if (mMoney.moneyView == 0) {
            costsRb.isChecked = true
        } else {
            incomeRb.isChecked = true
        }
        descriptionEt.setText(mMoney.description)
        sumEt.setText(mMoney.sum.toString())
    }

    private fun initLayout() {
        with(requireActivity()) {
            incomeRb = findViewById(R.id.umm_income_rb)
            costsRb = findViewById(R.id.umm_costs_rb)
            descriptionEt = findViewById(R.id.umm_description_et)
            sumEt = findViewById(R.id.umm_sum_et)
            updateBtn = findViewById(R.id.umm_update_btn)

            updateBtn.setOnClickListener {
                if (descriptionEt.text.toString().isNotEmpty()
                    && sumEt.text.toString().isNotEmpty()
                    && (incomeRb.isChecked || costsRb.isChecked)
                ) {
                    launchIO {
                        mMoney?.let { money ->
                            val sum = sumEt.text.toString().toDouble()
                            val view = if (incomeRb.isChecked) 1 else 0
                            val desc = descriptionEt.text.toString()
                            val difference = money.sum - sum
                            val total = calculateTotalMoney(
                                total = money.total,
                                difference = difference,
                                view = money.moneyView
                            )
                            val newMMoney =
                                MovingMoneyInfo(money.id, sum, total, view, desc, money.date)
                            movingMoneyDao?.updateMovingMoney(newMMoney)
                            recalculateMoney(money, difference)
                        }
                    }
                    findNavController().popBackStack()
                }
            }
        }
    }

    private suspend fun recalculateMoney(money: MovingMoneyInfo, difference: Double) {
        updateAfterDate(
            date = money.date,
            difference = difference,
            view = money.moneyView
        )
        updateAccount(difference = difference, view = money.moneyView)
    }

    private fun updateAccount(difference: Double, view: Int) {
        val totalCount = moneyDao?.getMoney()?.myMoney
        totalCount?.let {
            val money = calculateTotalMoney(
                total = it,
                difference = difference,
                view = view
            )
            moneyDao?.updateMoney(Money(myMoney = money))
        }
    }

    private suspend fun updateAfterDate(date: Long, difference: Double, view: Int) {
        val moneyList =
            movingMoneyDao?.getMovingMoneyAfterDate(date) ?: emptyList()
        for (movingMoney in moneyList) {
            val total = calculateTotalMoney(
                total = movingMoney.total,
                difference = difference,
                view = view
            )
            val newMMoney =
                MovingMoneyInfo(
                    movingMoney.id,
                    movingMoney.sum,
                    total,
                    movingMoney.moneyView,
                    movingMoney.description,
                    movingMoney.date
                )
            movingMoneyDao?.updateMovingMoney(newMMoney)
        }
    }

    private fun calculateTotalMoney(
        total: Double,
        difference: Double,
        view: Int
    ): Double {
        return if (view == 0) {
            total + difference
        } else {
            total - difference
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.delete_menu_item) {
            createAlertDialog(
                resources.getString(R.string.deletion),
                resources.getString(R.string.are_you_sure)
            )
                .setPositiveButton(resources.getString(R.string.delete)) { _, _ ->
                    launchIO {
                        mMoney?.let {
                            movingMoneyDao?.deleteMovingMoney(it)
                            recalculateMoney(it, it.sum)
                            findNavController().popBackStack()
                        }
                    }
                }
                .show()
        }
        return false
    }

    private fun createAlertDialog(title: String, message: String): MaterialAlertDialogBuilder {
        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setNegativeButton(resources.getString(R.string.cancel)) { dialog, _ ->
                dialog.cancel()
            }
    }
}