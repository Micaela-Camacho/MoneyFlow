package com.example.moneyflow.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transacciones")
data class Transaccion(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val descripcion: String, // tipo de gasto (sube, alquiler, etc)
    val monto: Double,
    val tipo: String, // si es egreso o ingreso de dinero
    val categoria: String,
    val fecha: Long // momento del registro
)

