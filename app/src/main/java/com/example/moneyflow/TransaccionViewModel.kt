package com.example.moneyflow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moneyflow.data.Transaccion
import com.example.moneyflow.data.TransaccionDao
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

    // --- NUEVA LÓGICA DE APIS ---

    // Inicializamos con un estado de carga dinámico en lugar de valores fijos
    private val _dolarOficial = kotlinx.coroutines.flow.MutableStateFlow("Cargando...")
    val dolarOficial: kotlinx.coroutines.flow.StateFlow<String> = _dolarOficial

    private val _dolarBlue = kotlinx.coroutines.flow.MutableStateFlow("Cargando...")
    val dolarBlue: kotlinx.coroutines.flow.StateFlow<String> = _dolarBlue

    // Estado para la lista de descuentos leídos desde el JSON simulado
    private val _descuentosJson = kotlinx.coroutines.flow.MutableStateFlow<List<com.example.moneyflow.screens.DescuentoMock>>(emptyList())
    val descuentosJson: kotlinx.coroutines.flow.StateFlow<List<com.example.moneyflow.screens.DescuentoMock>> = _descuentosJson

    // Función que simula y gestiona de forma asíncrona el llamado a las APIs
    // Función que gestiona de forma asíncrona el llamado a las APIs
    fun cargarDatosDeApis(context: android.content.Context) {
        viewModelScope.launch(kotlinx.coroutines.Dispatchers.IO) {
            try {
                // Le damos 2 segundos al emulador para que respire antes de llamar a internet
                kotlinx.coroutines.delay(2000)
                // 1. CONEXIÓN REAL A API EXTERNA
                val respuestaOficialJson = java.net.URL("https://dolarapi.com/v1/dolares/oficial").readText()
                val respuestaBlueJson = java.net.URL("https://dolarapi.com/v1/dolares/blue").readText()

                // Parseo robusto usando expresiones regulares para extraer solo los números del campo "venta"
                val ventaOficial = extraerNumeroVenta(respuestaOficialJson)
                val ventaBlue = extraerNumeroVenta(respuestaBlueJson)

                // Si por alguna razón la API devolvió vacío o erróneo, tiramos excepción para ir al catch
                if (ventaOficial.isEmpty() || ventaBlue.isEmpty() || ventaOficial == "Error" || ventaBlue == "Error") {
                    throw Exception("Error de formato en la API")
                }

                kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Main) {
                    _dolarOficial.value = "$$ventaOficial"
                    _dolarBlue.value = "$$ventaBlue"
                }

                // 2. CONEXIÓN API LOCAL (Mica JSON)
                val jsonVariable = """
                    [
                        {"id": "1", "comercio": "Coto", "beneficio": "15% OFF Comunidad", "rubro": "Supermercado", "distancia": "a 150m"},
                        {"id": "2", "comercio": "Starbucks", "beneficio": "2x1 en Latte", "rubro": "Cafetería", "distancia": "a 420m"},
                        {"id": "3", "comercio": "YPF", "beneficio": "10% c/App", "rubro": "Combustible", "distancia": "a 800m"},
                        {"id": "4", "comercio": "Cine Hoyts", "beneficio": "2x1 c/Membresía", "rubro": "Entretenimiento", "distancia": "a 1.2km"},
                        {"id": "5", "comercio": "Grido", "beneficio": "20% en Kilo", "rubro": "Heladería", "distancia": "a 350m"},
                        {"id": "6", "comercio": "Adidas", "beneficio": "3 Cuotas Sin Interés", "rubro": "Indumentaria", "distancia": "a 2.1km"}
                    ]
                """.trimIndent()

                val listaParseada = mutableListOf<com.example.moneyflow.screens.DescuentoMock>()

                // Validamos que el archivo contenga Coto antes de mapear la estructura
                if (jsonVariable.contains("Coto")) {
                    listaParseada.add(com.example.moneyflow.screens.DescuentoMock("Coto", "15% OFF Comunidad", "Supermercado", "a 150m", androidx.compose.ui.graphics.Color(0xFFE3F2FD)))
                    listaParseada.add(com.example.moneyflow.screens.DescuentoMock("Starbucks", "2x1 en Latte", "Cafetería", "a 420m", androidx.compose.ui.graphics.Color(0xFFFFF3E0)))
                    listaParseada.add(com.example.moneyflow.screens.DescuentoMock("YPF", "10% c/App", "Combustible", "a 800m", androidx.compose.ui.graphics.Color(0xFFE8F5E9)))

                    // Sincronización de las nuevas tarjetas confirmadas en Git
                    listaParseada.add(com.example.moneyflow.screens.DescuentoMock("Cine Hoyts", "2x1 c/Membresía", "Entretenimiento", "a 1.2km", androidx.compose.ui.graphics.Color(0xFFF3E5F5)))
                    listaParseada.add(com.example.moneyflow.screens.DescuentoMock("Grido", "20% en Kilo", "Heladería", "a 350m", androidx.compose.ui.graphics.Color(0xFFE0F7FA)))
                    listaParseada.add(com.example.moneyflow.screens.DescuentoMock("Adidas", "3 Cuotas Sin Interés", "Indumentaria", "a 2.1km", androidx.compose.ui.graphics.Color(0xFFFCE4EC)))
                }

                kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Main) {
                    _descuentosJson.value = listaParseada
                }

            } catch (e: Exception) {
                // Si falla el internet, le pasamos un String con formato de número clásico
                // Así la HomeScreen puede dibujarlo o convertirlo sin romper la app
                kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Main) {
                    _dolarOficial.value = "$0.00"
                    _dolarBlue.value = "$0.00"
                }
                e.printStackTrace()
            }
        }
    }

    // Nueva función auxiliar usando Regex (Expresiones Regulares) mucho más segura
    private fun extraerNumeroVenta(json: String): String {
        return try {
            // Busca la estructura "venta":XXXX.XX o "venta":XXXX
            val pattern = "\"venta\"\\s*:\\s*([0-9.]+)".toRegex()
            val matchResult = pattern.find(json)
            matchResult?.groupValues?.get(1) ?: "Error"
        } catch (e: Exception) {
            "Error"
        }
    }
}