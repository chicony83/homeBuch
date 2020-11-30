package com.chico.homebuch.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.chico.homebuch.database.entity.Money

@Dao
interface MoneyDao {
    @Insert
    fun addMoney(money: Money)

    @Query("SELECT * FROM my_money_table WHERE id = 1")
    fun getMoney(): Money

    @Update
    fun updateMoney(money: Money)
}