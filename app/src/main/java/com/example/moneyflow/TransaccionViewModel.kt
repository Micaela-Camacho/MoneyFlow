package com.example.moneyflow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moneyflow.data.Transaccion
import com.example.moneyflow.data.TransaccionDao
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TransaccionViewModel(private val transaccionDao: TransaccionDao) : ViewModel() {

    // 1. StateFlow vivo para poder mapearlo eficientemente
    val todasLasTransacciones = transaccionDao.obtenerTodas()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // 2. CONTADOR REAL DE GASTOS: Filtra y suma en tiempo real solo los EGRESOS
    val totalGastos = todasLasTransacciones.map { lista ->
        lista.filter { it.tipo == "EGRESO" }.sumOf { it.monto }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    // 3. CONTADOR REAL DE INGRESOS EXTRA: Filtra y suma los ingresos manuales
    val totalIngresosExtra = todasLasTransacciones.map { lista ->
        lista.filter { it.tipo == "INGRESO" }.sumOf { it.monto }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    // 4. CONTADOR REAL DE AHORROS: Filtra y suma lo destinado a metas de ahorro
    val totalAhorros = todasLasTransacciones.map { lista ->
        lista.filter { it.tipo == "AHORRO" }.sumOf { it.monto }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)


    // 5. Función para insertar usando Corrutinas
    fun agregarTransaccion(
        usuarioId: Int,
        descripcion: String,
        monto: Double,
        tipo: String,
        categoria: String
    ) {
        val nuevaTransaccion = Transaccion(
            usuarioId = usuarioId,
            descripcion = descripcion,
            monto = monto,
            tipo = tipo,
            categoria = categoria,
            fecha = System.currentTimeMillis()
        )

        viewModelScope.launch {
            transaccionDao.insertar(nuevaTransaccion)
        }
    }

    // 6. Función para borrar
    fun eliminarTransaccion(transaccion: Transaccion) {
        viewModelScope.launch {
            transaccionDao.borrar(transaccion)
        }
    }
}