package com.chico.homebuch.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "my_money_table")
data class Money(
    @PrimaryKey
    val id: Int = 1,
    val myMoney: Double
)
