package com.example.moneyflow.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Transaccion::class], version = 1, exportSchema = false)
abstract class MoneyFlowDatabase : RoomDatabase() {
    abstract fun transaccionDao(): TransaccionDao

    companion object {
        @Volatile
        private var INSTANCE: MoneyFlowDatabase? = null

        fun obtenerBaseDatos(context: Context): MoneyFlowDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MoneyFlowDatabase::class.java,
                    "money_flow_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
