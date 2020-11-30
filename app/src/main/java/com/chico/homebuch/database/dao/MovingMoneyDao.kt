package com.chico.homebuch.database.dao

import androidx.room.*
import com.chico.homebuch.database.entity.MovingMoneyInfo

@Dao
interface MovingMoneyDao {
    @Query("SELECT * FROM moving_money_table ORDER BY date DESC")
    suspend fun getMovingMoneyInfo(): List<MovingMoneyInfo>

    @Query("SELECT * FROM moving_money_table WHERE moneyView = :view")
    suspend fun getMovingMoneyInfoIncomeOrCosts(view: Int): List<MovingMoneyInfo>

    @Insert
    suspend fun addMovingMoney(movingMoneyInfo: MovingMoneyInfo)

    @Delete
    suspend fun deleteMovingMoney(movingMoneyInfo: MovingMoneyInfo)

    @Update
    suspend fun updateMovingMoney(movingMoneyInfo: MovingMoneyInfo)
}