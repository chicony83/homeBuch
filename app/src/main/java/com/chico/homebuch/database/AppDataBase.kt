package com.chico.homebuch.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.chico.homebuch.database.dao.MoneyDao
import com.chico.homebuch.database.dao.MovingMoneyDao
import com.chico.homebuch.database.entity.Money
import com.chico.homebuch.database.entity.MovingMoneyInfo

@Database(entities = [MovingMoneyInfo::class, Money::class], version = 3, exportSchema = false)
abstract class AppDataBase : RoomDatabase() {
    abstract fun getMovingMoneyDao(): MovingMoneyDao
    abstract fun getMoneyDao(): MoneyDao

    companion object {
        private var INSTANCE : AppDataBase? = null
        fun getInstance(context: Context): AppDataBase? {
            if (INSTANCE == null){
                synchronized(AppDataBase::class){
                    INSTANCE =  Room.databaseBuilder(
                        context,
                        AppDataBase::class.java,
                        "moving_money_db"
                    )
                        .build()
                }
            }
            return INSTANCE
        }
    }
}
