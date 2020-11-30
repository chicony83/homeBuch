package com.chico.homebuch.adapter

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.chico.homebuch.R
import com.chico.homebuch.database.entity.MovingMoneyInfo
import java.text.SimpleDateFormat

class MoneyAdapter(val listener: Listener) : RecyclerView.Adapter<MoneyAdapter.MoneyVH>() {

    private var moneyList = ArrayList<MovingMoneyInfo>()

    fun updateList(list: List<MovingMoneyInfo>) {
        moneyList = list as ArrayList<MovingMoneyInfo>
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoneyVH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.money_item, parent, false)
        return MoneyVH(view)
    }

    override fun getItemCount() = moneyList.size

    override fun onBindViewHolder(holder: MoneyVH, position: Int) {
        holder.bind(moneyList[position])
    }

    inner class MoneyVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val dateTv: TextView
        private val descriptionTv: TextView
        private val totalTv: TextView
        private val sumTv: TextView
        private val constraintLayout: ConstraintLayout

        init {
            with(itemView) {
                dateTv = findViewById(R.id.date_tv)
                descriptionTv = findViewById(R.id.description_tv)
                totalTv = findViewById(R.id.total_tv)
                sumTv = findViewById(R.id.sum_tv)
                constraintLayout = findViewById(R.id.constraint_layout)

                setOnClickListener {
                    listener.onClick(moneyList[layoutPosition])
                }
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(money: MovingMoneyInfo) {
            val dataTime = transferDataTime(money.date)
            dateTv.text = dataTime
            descriptionTv.text = money.description
            totalTv.text = "Total amount ${money.total}"
            sumTv.text = "Transaction ${money.sum}"

            if (money.moneyView == 0) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    constraintLayout.setBackgroundColor(
                        itemView.resources.getColor(
                            R.color.redColor,
                            itemView.resources.newTheme()
                        )
                    )
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    constraintLayout.setBackgroundColor(
                        itemView.resources.getColor(
                            R.color.greenColor,
                            itemView.resources.newTheme()
                        )
                    )
                }
            }
        }

        @SuppressLint("SimpleDateFormat")
        private fun transferDataTime(long:Long): String? {
            val sdf = SimpleDateFormat("dd/M/yyyy HH:mm:ss")
            return sdf.format(long)
        }
    }

    interface Listener {
        fun onClick(money: MovingMoneyInfo)
    }

}