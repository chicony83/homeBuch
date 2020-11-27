package com.chico.homebuch.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.chico.homebuch.database.entity.MovingMoneyInfo

@Dao
interface MovingMoneyDao {
    @Query("SELECT * FROM moving_money_table")
    suspend fun getMovingMoneyInfo(): List<MovingMoneyInfo>

    @Insert
    suspend fun addMovingMoney(movingMoneyInfo: MovingMoneyInfo)
}