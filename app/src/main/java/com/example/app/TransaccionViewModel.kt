package com.example.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app.data.Transaccion
import com.example.app.data.TransaccionDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TransaccionViewModel(private val transaccionDao: TransaccionDao) : ViewModel() {

    // ID del usuario activo para filtrar los datos
    private val _usuarioIdActual = MutableStateFlow<Int?>(null)

    // Flujo que reacciona automáticamente cada vez que cambia el usuarioId
    @OptIn(ExperimentalCoroutinesApi::class)
    val transaccionesUsuario = _usuarioIdActual.flatMapLatest { id ->
        if (id != null) transaccionDao.obtenerPorUsuario(id)
        else flowOf(emptyList())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Totales calculados sobre la lista filtrada por usuario
    val totalGastos = transaccionesUsuario.map { lista ->
        lista.filter { it.tipo == "EGRESO" }.sumOf { it.monto }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val totalIngresosExtra = transaccionesUsuario.map { lista ->
        lista.filter { it.tipo == "INGRESO" }.sumOf { it.monto }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val totalAhorros = transaccionesUsuario.map { lista ->
        lista.filter { it.tipo == "AHORRO" }.sumOf { it.monto }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    // Función para actualizar el ID cuando el usuario se loguea
    fun setUsuarioId(id: Int) {
        _usuarioIdActual.value = id
    }

    // Función para insertar
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

    // Función para borrar
    fun eliminarTransaccion(transaccion: Transaccion) {
        viewModelScope.launch {
            transaccionDao.borrar(transaccion)
        }
    }
}

