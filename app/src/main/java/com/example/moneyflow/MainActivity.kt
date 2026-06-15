package com.example.moneyflow

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.moneyflow.navigation.MoneyFlowScreen
import com.example.moneyflow.screens.*
import com.example.moneyflow.ui.theme.MoneyFlowTheme
import kotlin.math.sqrt

// Agregamos "SensorEventListener" para indicarle a Android que esta actividad va a escuchar hardware
class MainActivity : ComponentActivity(), SensorEventListener {

    // Variables del sistema de sensores de Android
    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null

    // Variables matemáticas para calcular la fuerza del sacudón (Shake)
    private var accelerationCurrent = SensorManager.GRAVITY_EARTH
    private var accelerationLast = SensorManager.GRAVITY_EARTH
    private var shakeThreshold = 12.0f // Sensibilidad: si es muy sensible podés subirlo a 14 o 15

    // Estado observable de Compose que va a cambiar a TRUE cuando detecte la rotación/movimiento brusco
    private var onShakeEvent = mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializamos el gestor de sensores nativo
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        // 1. Conecta con la base de datos global utilizando Application class
        val app = application as MoneyFlowApp
        val database = app.database

        // 2. Fábrica capaz de construir ambos controladores pasándoles sus respectivos DAOs
        val factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return when {
                    modelClass.isAssignableFrom(TransaccionViewModel::class.java) -> {
                        TransaccionViewModel(database.transaccionDao()) as T
                    }
                    modelClass.isAssignableFrom(UsuarioViewModel::class.java) -> {
                        UsuarioViewModel(database.usuarioDao()) as T
                    }
                    else -> throw IllegalArgumentException("ViewModel Desconocido")
                }
            }
        }

        // 3. Inicializa los controladores de datos (Backend)
        val transaccionViewModel = ViewModelProvider(this, factory)[TransaccionViewModel::class.java]
        val usuarioViewModel = ViewModelProvider(this, factory)[UsuarioViewModel::class.java]

        setContent {
            MoneyFlowTheme {
                // Estado local para saber qué pantalla dibujar (arranca en la Splash Screen)
                var currentScreen by remember { mutableStateOf<MoneyFlowScreen>(MoneyFlowScreen.Splash) }

                // Compose "mira" constantemente si esta variable cambia
                val hasShaked by remember { onShakeEvent }

                when (currentScreen) {
                    MoneyFlowScreen.Splash -> SplashScreen(
                        onStartClick = { currentScreen = MoneyFlowScreen.Login }
                    )

                    // El Login ahora podría usar el usuarioViewModel más adelante para validar
                    MoneyFlowScreen.Login -> LoginScreen(
                        viewModel = usuarioViewModel,
                        onLoginClick = { currentScreen = MoneyFlowScreen.Home },
                        onRegisterClick = { currentScreen = MoneyFlowScreen.Register }
                    )

                    // El Registro va a usar el usuarioViewModel para insertar el nuevo usuario
                    MoneyFlowScreen.Register -> RegisterScreen(
                        viewModel = usuarioViewModel,
                        onBackClick = { currentScreen = MoneyFlowScreen.Login },
                        onCreateAccountClick = { currentScreen = MoneyFlowScreen.Home },
                        onLoginClick = { currentScreen = MoneyFlowScreen.Login }
                    )

                    // Pasa transaccionViewModel para los números y usuarioViewModel para el nombre/sueldo
                    MoneyFlowScreen.Home -> HomeScreen(
                        transaccionViewModel = transaccionViewModel,
                        usuarioViewModel = usuarioViewModel,
                        onNavigate = { currentScreen = it }
                    )

                    // PASAMOS LAS VARIABLES DEL SENSOR A LA PANTALLA DE AGREGAR GASTO
                    MoneyFlowScreen.AddExpense -> AddExpenseScreen(
                        viewModel = transaccionViewModel,
                        onNavigate = { currentScreen = it },
                        shakeTriggered = hasShaked,          // Pasa si se sacudió o no
                        onPositionReset = { onShakeEvent.value = false } // Función para apagar el gatillo
                    )

                    MoneyFlowScreen.Savings -> SavingsScreen(
                        onNavigate = { currentScreen = it }
                    )

                    // Pasa usuarioViewModel para ver y editar los datos del perfil
                    MoneyFlowScreen.Profile -> ProfileScreen(
                        viewModel = usuarioViewModel,
                        onNavigate = { currentScreen = it }
                    )
                }
            }
        }
    }

    // --- MANEJO CRÍTICO DEL CICLO DE VIDA (Para la defensa del TP) ---

    // Activamos el sensor cuando la app pasa al frente
    override fun onResume() {
        super.onResume()
        accelerometer?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    // Apagamos el sensor si el usuario minimiza la app o bloquea el celu (Evita consumir RAM y batería)
    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    // --- CAPTURA DE MOVIMIENTO EN EJES X, Y, Z ---
    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null && event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            accelerationLast = accelerationCurrent
            // Pitágoras para calcular la aceleración neta restando la gravedad
            accelerationCurrent = sqrt((x * x + y * y + z * z).toDouble()).toFloat()
            val delta: Float = accelerationCurrent - accelerationLast

            // Si el movimiento supera el umbral, activamos el evento
            if (delta > shakeThreshold) {
                onShakeEvent.value = true
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Obligatorio por la interfaz, pero no requiere lógica para este caso
    }
}