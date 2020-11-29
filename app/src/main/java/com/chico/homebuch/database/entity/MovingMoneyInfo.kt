package com.chico.homebuch.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "moving_money_table")
data class MovingMoneyInfo(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    val total: Double,
    val moneyView: Int,
    val description: String,
    val date: Long
)