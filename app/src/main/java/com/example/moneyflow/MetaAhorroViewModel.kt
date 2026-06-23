package com.example.moneyflow

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moneyflow.data.MetaAhorro
import com.example.moneyflow.data.MetaAhorroDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MetaAhorroViewModel(private val metaAhorroDao: MetaAhorroDao) : ViewModel() {

    var metaSeleccionadaId by mutableStateOf<Int?>(null)
        private set

    fun seleccionarMeta(metaId: Int) {
        metaSeleccionadaId = metaId
    }

    fun obtenerMetasPorUsuario(usuarioId: Int): Flow<List<MetaAhorro>> {
        return metaAhorroDao.obtenerMetasPorUsuario(usuarioId)
    }

    fun obtenerMetaPorId(metaId: Int): Flow<MetaAhorro?> {
        return metaAhorroDao.obtenerMetaPorId(metaId)
    }

    fun crearMeta(
        usuarioId: Int,
        nombre: String,
        montoObjetivo: Double,
        montoActual: Double
    ) {
        val nuevaMeta = MetaAhorro(
            usuarioId = usuarioId,
            nombre = nombre,
            montoObjetivo = montoObjetivo,
            montoActual = montoActual,
            fechaCreacion = System.currentTimeMillis()
        )

        viewModelScope.launch {
            metaAhorroDao.insertarMeta(nuevaMeta)
        }
    }

    fun actualizarMeta(meta: MetaAhorro) {
        viewModelScope.launch {
            metaAhorroDao.actualizarMeta(meta)
        }
    }

    fun eliminarMeta(metaId: Int) {
        viewModelScope.launch {
            metaAhorroDao.eliminarMeta(metaId)
        }
    }
}