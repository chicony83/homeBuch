package com.chico.homebuch.ui

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.chico.homebuch.R
import com.chico.homebuch.constants.Const.MOVING_MONEY_KEY
import com.chico.homebuch.database.AppDataBase
import com.chico.homebuch.database.entity.MovingMoneyInfo
import com.chico.homebuch.utils.launchIO

class UpdateMoneyMovingFragment : Fragment() {

    private val movingMoneyDao by lazy {
        AppDataBase.getInstance(requireContext())?.getMovingMoneyDao()
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
                            val desc = descriptionEt.text.toString()
                            val sum = sumEt.text.toString().toDouble()
                            val total = if (money.moneyView == 0) {
                                money.total.plus(money.sum).minus(sum)
                            } else {
                                money.total.minus(money.sum).plus(sum)
                            }
                            val view = if (incomeRb.isChecked) 1 else 0
                            val newMMoney =
                                MovingMoneyInfo(money.id, sum, total, view, desc, money.date)
                            movingMoneyDao?.updateMovingMoney(newMMoney)
                        }
                    }
                    findNavController().popBackStack()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.delete_menu_item) {
            launchIO {
                mMoney?.let {
                    movingMoneyDao?.deleteMovingMoney(it)
                    findNavController().popBackStack()
                }
            }
        }
        return false
    }
}