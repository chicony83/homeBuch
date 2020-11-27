package com.chico.homebuch.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.chico.homebuch.R
import com.chico.homebuch.database.entity.MovingMoneyInfo

class MoneyAdapter : RecyclerView.Adapter<MoneyAdapter.MoneyVH>() {

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

    class MoneyVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val dateTv: TextView
        private val descriptionTv: TextView
        private val totalTv: TextView
        private val constraintLayout: ConstraintLayout

        init {
            with(itemView) {
                dateTv = findViewById(R.id.date_tv)
                descriptionTv = findViewById(R.id.description_tv)
                totalTv = findViewById(R.id.total_tv)
                constraintLayout = findViewById(R.id.constraint_layout)
            }
        }

        fun bind(money: MovingMoneyInfo) {
            dateTv.text = money.date
            descriptionTv.text = money.description
            totalTv.text = money.total.toString()

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
    }
}