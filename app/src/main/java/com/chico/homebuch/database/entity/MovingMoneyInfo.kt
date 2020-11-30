package com.chico.homebuch.database.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "moving_money_table")
data class MovingMoneyInfo(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    val sum: Double,
    val total: Double,
    val moneyView: Int,
    val description: String,
    val date: Long
) : Parcelable