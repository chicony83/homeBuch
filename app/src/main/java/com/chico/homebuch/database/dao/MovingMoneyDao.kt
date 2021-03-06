package com.chico.homebuch.database.dao

import androidx.room.*
import com.chico.homebuch.database.entity.MovingMoneyInfo

@Dao
interface MovingMoneyDao {
    @Query("SELECT * FROM moving_money_table ORDER BY date DESC")
    suspend fun getMovingMoneyInfo(): List<MovingMoneyInfo>

    @Query("SELECT * FROM moving_money_table WHERE moneyView = :view ORDER BY date DESC")
    suspend fun getMovingMoneyInfoIncomeOrCosts(view: Int): List<MovingMoneyInfo>

    @Query("SELECT * FROM moving_money_table WHERE date > :date ORDER BY date DESC")
    suspend fun getMovingMoneyAfterDate(date: Long): List<MovingMoneyInfo>

    @Insert
    suspend fun addMovingMoney(movingMoneyInfo: MovingMoneyInfo)

    @Delete
    suspend fun deleteMovingMoney(movingMoneyInfo: MovingMoneyInfo)

    @Update
    suspend fun updateMovingMoney(movingMoneyInfo: MovingMoneyInfo)
}