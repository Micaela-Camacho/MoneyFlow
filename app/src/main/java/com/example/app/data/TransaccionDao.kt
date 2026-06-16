package com.example.app.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TransaccionDao {
    @Query("SELECT * FROM transacciones ORDER BY fecha DESC")
    fun obtenerTodas(): Flow<List<Transaccion>>
    @Query("SELECT * FROM transacciones WHERE usuarioId = :usuarioId ORDER BY fecha DESC")
    fun obtenerPorUsuario(usuarioId: Int): Flow<List<Transaccion>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(transaccion: Transaccion)

    @Delete
    suspend fun borrar(transaccion: Transaccion)

}