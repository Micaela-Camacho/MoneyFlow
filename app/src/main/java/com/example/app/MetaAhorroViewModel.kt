package com.example.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app.data.MetaAhorro
import com.example.app.data.MetaAhorroDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MetaAhorroViewModel(private val metaAhorroDao: MetaAhorroDao) : ViewModel() {

    fun obtenerMetasPorUsuario(usuarioId: Int): Flow<List<MetaAhorro>> {
        return metaAhorroDao.obtenerMetasPorUsuario(usuarioId)
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
}