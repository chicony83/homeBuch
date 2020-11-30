package com.chico.homebuch.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.chico.homebuch.R
import com.chico.homebuch.constants.Const.APP_PREF
import com.chico.homebuch.constants.Const.PREF_NAME
import com.chico.homebuch.database.AppDataBase
import com.chico.homebuch.database.entity.Money
import com.chico.homebuch.utils.launchIO

class FirstStartFragment : Fragment() {

    private val moneyDao by lazy { AppDataBase.getInstance(requireContext())?.getMoneyDao() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_first_start, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val startBtn = requireActivity().findViewById<Button>(R.id.start_btn)
        val myMoneyEt = requireActivity().findViewById<EditText>(R.id.my_money_et)
        startBtn?.setOnClickListener {
            if (myMoneyEt.text.toString().isNotEmpty()){
                val mySp = activity?.getSharedPreferences(APP_PREF, Context.MODE_PRIVATE)
                mySp?.edit()?.putInt(PREF_NAME, 1)?.apply()

                launchIO {
                    moneyDao?.addMoney(Money(
                        1,
                        myMoneyEt.text.toString().toDouble()
                    ))
                }

                findNavController().popBackStack()
                findNavController().navigate(R.id.reportFragment)
            } else {
                Toast.makeText(context, "enter money", Toast.LENGTH_SHORT).show()
            }
        }
    }
}