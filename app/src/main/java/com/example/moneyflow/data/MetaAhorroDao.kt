package com.example.moneyflow.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MetaAhorroDao {

    @Query("SELECT * FROM metas_ahorro WHERE usuarioId = :usuarioId ORDER BY fechaCreacion DESC")
    fun obtenerMetasPorUsuario(usuarioId: Int): Flow<List<MetaAhorro>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarMeta(meta: MetaAhorro)
}