package com.example.moneyflow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moneyflow.data.Transaccion
import com.example.moneyflow.data.TransaccionDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class TransaccionViewModel(private val transaccionDao: TransaccionDao) : ViewModel() {

    // 1. Traemos el flujo vivo de transacciones desde el DAO
    val todasLasTransacciones: Flow<List<Transaccion>> = transaccionDao.obtenerTodas()

    // 2. Función para insertar un gasto/ingreso usando Corrutinas en segundo plano
    fun agregarTransaccion(descripcion: String, monto: Double, tipo: String, categoria: String) {
        val nuevaTransaccion = Transaccion(
            descripcion = descripcion,
            monto = monto,
            tipo = tipo,
            categoria = categoria,
            fecha = System.currentTimeMillis() // Captura el milisegundo exacto de ahora
        )

        // viewModelScope.launch inicia la corrutina (el segundo plano)
        viewModelScope.launch {
            transaccionDao.insertar(nuevaTransaccion)
        }
    }

    // 3. Función para borrar un movimiento
    fun eliminarTransaccion(transaccion: Transaccion) {
        viewModelScope.launch {
            transaccionDao.borrar(transaccion)
        }
    }
}