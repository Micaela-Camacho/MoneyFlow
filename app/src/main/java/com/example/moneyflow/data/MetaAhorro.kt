package com.example.moneyflow.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "metas_ahorro")
data class MetaAhorro(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val usuarioId: Int,
    val nombre: String,
    val montoObjetivo: Double,
    val montoActual: Double,
    val fechaCreacion: Long
)