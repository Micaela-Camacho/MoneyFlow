package com.example.moneyflow.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Transaccion::class, Usuario::class, MetaAhorro::class],
    version = 4,
    exportSchema = false
)abstract class MoneyFlowDatabase : RoomDatabase() {
    abstract fun transaccionDao(): TransaccionDao

    abstract fun usuarioDao(): UsuarioDao
    abstract fun metaAhorroDao(): MetaAhorroDao

    companion object {
        @Volatile
        private var INSTANCE: MoneyFlowDatabase? = null

        fun obtenerBaseDatos(context: Context): MoneyFlowDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MoneyFlowDatabase::class.java,
                    "money_flow_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
