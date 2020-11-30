package com.chico.homebuch.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.chico.homebuch.R
import com.chico.homebuch.constants.Const.TOTAL_MONEY_KEY
import com.chico.homebuch.database.AppDataBase
import com.chico.homebuch.database.entity.Money
import com.chico.homebuch.database.entity.MovingMoneyInfo
import com.chico.homebuch.utils.launchIO

class AddMoneyMovingFragment : Fragment() {
    private val moneyMovingDao by lazy {
        AppDataBase.getInstance(requireContext())?.getMovingMoneyDao()
    }
    private val moneyDao by lazy { AppDataBase.getInstance(requireContext())?.getMoneyDao() }

    private lateinit var descriptionEt: EditText
    private lateinit var sumEt: EditText
    private lateinit var incomingRb: RadioButton
    private lateinit var costRb: RadioButton
    private lateinit var addBtn: Button

    private var total = 0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_new_money_moving, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initLayout()

        total = arguments?.getDouble(TOTAL_MONEY_KEY) ?: 0.0
    }

    private fun initLayout() {
        with(requireActivity()) {
            descriptionEt = findViewById(R.id.description_et)
            sumEt = findViewById(R.id.sum_et)
            incomingRb = findViewById(R.id.incomingMoney_radio)
            costRb = findViewById(R.id.costMoney_radio)
            addBtn = findViewById(R.id.addingInBase_button)
            addBtn.setOnClickListener {
                addInDataBase()
            }
        }
    }

    private fun addInDataBase() {
        if (descriptionEt.text.isNotEmpty() && sumEt.text.isNotEmpty() && (incomingRb.isChecked || costRb.isChecked)) {
            val desc = descriptionEt.text.toString()
            val sum = sumEt.text.toString().toDouble()
            val view = if (incomingRb.isChecked) {
                total += sum
                1
            } else {
                total -= sum
                0
            }
            val curDate = System.currentTimeMillis()

            launchIO {
                moneyMovingDao?.addMovingMoney(
                    MovingMoneyInfo(
                        sum = sum,
                        total = total,
                        moneyView = view,
                        description = desc,
                        date = curDate
                    )
                )
            }

            launchIO {
                moneyDao?.updateMoney(Money(myMoney = total))
            }

            findNavController().popBackStack()
        } else {
            Toast.makeText(context, "Заполните все поля", Toast.LENGTH_SHORT).show()
        }
    }
}