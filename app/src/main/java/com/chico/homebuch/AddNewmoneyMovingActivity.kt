package com.chico.homebuch

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import com.chico.homebuch.database.AppDataBase
import com.chico.homebuch.database.entity.MovingMoneyInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddNewmoneyMovingActivity : AppCompatActivity() {

    private val moneyDao by lazy { AppDataBase.getInstance(applicationContext)?.getMovingMoneyDao() }

    private lateinit var descriptionEt: EditText
    private lateinit var sumEt: EditText
    private lateinit var incomingRb: RadioButton
    private lateinit var costRb: RadioButton
    private lateinit var addBtn: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_money_moving)
        initLayout()
    }

    private fun initLayout() {
        descriptionEt = findViewById(R.id.description_et)
        sumEt = findViewById(R.id.sum_et)
        incomingRb = findViewById(R.id.incomingMoney_radio)
        costRb = findViewById(R.id.costMoney_radio)
        addBtn = findViewById(R.id.addingInBase_button)
        addBtn.setOnClickListener {
            addInDataBase()
        }
    }

    private fun addInDataBase() {
        if (descriptionEt.text.isNotEmpty() && sumEt.text.isNotEmpty() && (incomingRb.isChecked || costRb.isChecked)) {
            val desc = descriptionEt.text.toString()
            val sum = sumEt.text.toString().toDouble()
            val view = if (incomingRb.isChecked) {
                1
            } else {
                0
            }
            val curDate = System.currentTimeMillis()

            CoroutineScope(Dispatchers.IO).launch {
//                moneyDao?.addMovingMoney(
//                    MovingMoneyInfo(
//                        sum = sum,
//                        moneyView = view,
//                        description = desc,
//                        date = curDate
//                    )
//                )
            }
            setResult(Activity.RESULT_OK)
            finish()
        } else {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
        }
    }
}