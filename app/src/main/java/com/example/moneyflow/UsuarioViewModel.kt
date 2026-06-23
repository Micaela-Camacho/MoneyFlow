package com.example.moneyflow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moneyflow.data.Usuario
import com.example.moneyflow.data.UsuarioDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Le pasa el UsuarioDao por constructor para que pueda hacer consultas a las tablas
class UsuarioViewModel(private val usuarioDao: UsuarioDao) : ViewModel() {

    // 1. ESTADOS GLOBALES (Se encargan de avisarle a la pantalla quién está usando la app)

    // Guarda el ID del usuario activo. Empieza en null (nadie logueado).
    private val _usuarioLogueadoId = MutableStateFlow<Int?>(null)
    val usuarioLogueadoId: StateFlow<Int?> = _usuarioLogueadoId.asStateFlow()

    // Guarda el objeto completo del Usuario actual (nombre, email, sueldo, etc.)
    private val _usuarioActual = MutableStateFlow<Usuario?>(null)
    val usuarioActual: StateFlow<Usuario?> = _usuarioActual.asStateFlow()


    // 2. FUNCIONES DE LÓGICA DE NEGOCIO

    /**
     * Función para la pantalla de Registro.
     * Recibe los datos del formulario y un "onResult" para avisarle a la pantalla si salió todo bien
     */
    fun registrarNuevoUsuario(
        nombre: String,
        email: String,
        contrasenia: String,
        sueldo: Double,
        onResult: (Boolean) -> Unit
    ) {
        // Abre una corrutina (hilo paralelo) para que la base de datos no tilde la pantalla
        viewModelScope.launch {
            try {
                // Crea el objeto molde con los datos del formulario
                val nuevoUsuario = Usuario(
                    nombre = nombre,
                    email = email,
                    contrasenia = contrasenia,
                    sueldoMensual = sueldo
                )

                // Le pide al DAO que lo inserte en la base de datos
                val idGenerado = usuarioDao.registrarUsuario(nuevoUsuario)

                if (idGenerado > 0) {
                    // Si el ID es válido, significa que se guardó con éxito.
                    _usuarioLogueadoId.value = idGenerado.toInt()
                    observarUsuario(idGenerado.toInt())
                    onResult(true)
                } else {
                    onResult(false)
                }
            } catch (e: Exception) {
                onResult(false)
            }
        }
    }

    // Función para el Login
    fun iniciarSesion(email: String, contrasenia: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val usuario = usuarioDao.login(email, contrasenia)
            if (usuario != null) {
                _usuarioLogueadoId.value = usuario.id
                observarUsuario(usuario.id)
                onResult(true)
            } else {
                onResult(false)
            }
        }
    }

    // Cierre de sesión
    fun cerrarSesion() {
        viewModelScope.launch{
            _usuarioLogueadoId.value = null
            _usuarioActual.value = null
        }
    }

    // Escucha al usuario en tiempo real
    private fun observarUsuario(id: Int) {
        viewModelScope.launch {
            usuarioDao.obtenerUsuarioPorId(id).collect { usuario ->
                _usuarioActual.value = usuario
            }
        }
    }

    fun actualizarSueldoMensual(nuevoSueldo: Double) {
        val id = _usuarioLogueadoId.value ?: return

        viewModelScope.launch {
            usuarioDao.actualizarSueldo(id, nuevoSueldo)
        }
    }
}