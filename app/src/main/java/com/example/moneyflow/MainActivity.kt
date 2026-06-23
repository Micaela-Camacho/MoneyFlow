package com.example.moneyflow
import com.example.moneyflow.MetaAhorroViewModel

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
    private var shakeThreshold = 2.5f // Sensibilidad: si es muy sensible podés subirlo a 14 o 15 ( Modificado 22/6)

    // Estado observable de Compose que va a cambiar a TRUE cuando detecte la rotación/movimiento brusco
    private var onShakeEvent = mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializa el gestor de sensores nativo
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
                    modelClass.isAssignableFrom(MetaAhorroViewModel::class.java) -> {
                        MetaAhorroViewModel(database.metaAhorroDao()) as T
                    }
                    else -> throw IllegalArgumentException("ViewModel Desconocido")
                }
            }
        }

        // 3. Inicializa los controladores de datos (Backend)
        val transaccionViewModel = ViewModelProvider(this, factory)[TransaccionViewModel::class.java]
        val usuarioViewModel = ViewModelProvider(this, factory)[UsuarioViewModel::class.java]
        val metaAhorroViewModel = ViewModelProvider(this, factory)[MetaAhorroViewModel::class.java]

        setContent {
            MoneyFlowTheme {
                //  1. Inicializa SharedPreferences para guardar la configuración
                val prefs = remember { getSharedPreferences("MoneyFlowPrefs", Context.MODE_PRIVATE) }
                val yaVioOnboarding = remember { prefs.getBoolean("onboarding_completo", false) }

                //  2. Si ya lo vio arranca en Login, sino arranca en el Splash (Onboarding)
                var currentScreen by remember {
                    mutableStateOf<MoneyFlowScreen>(
                        if (yaVioOnboarding) MoneyFlowScreen.Login else MoneyFlowScreen.Splash
                    )
                }

                // Compose "mira" constantemente si esta variable cambia
                val hasShaked by remember { onShakeEvent }

                when (currentScreen) {
                    MoneyFlowScreen.Splash -> SplashScreen(
                        onStartClick = {
                            //  3. Al hacer clic en Comenzar, guarda el estado para la próxima vez
                            prefs.edit().putBoolean("onboarding_completo", true).apply()
                            currentScreen = MoneyFlowScreen.Login
                        }
                    )

                    // El Login ahora podría usar el usuarioViewModel más adelante para validar
                    MoneyFlowScreen.Login -> LoginScreen(
                        viewModel = usuarioViewModel,
                        onLoginClick = {
                            // 1. Obtiene el ID del usuario recién logueado
                            val id = usuarioViewModel.usuarioLogueadoId.value
                            if (id != null) {
                                // 2. Le avisa al ViewModel de transacciones qué usuario filtrar
                                transaccionViewModel.setUsuarioId(id)
                            }
                            currentScreen = MoneyFlowScreen.Home
                        },
                        onRegisterClick = { currentScreen = MoneyFlowScreen.Register },
                        onForgotPasswordClick = { currentScreen = MoneyFlowScreen.ForgotPassword }
                    )
                    // El Registro va a usar el usuarioViewModel para insertar el nuevo usuario
                    MoneyFlowScreen.Register -> RegisterScreen(
                        viewModel = usuarioViewModel,
                        onBackClick = { currentScreen = MoneyFlowScreen.Login },
                        onCreateAccountClick = { // 1. Obtiene el ID del nuevo usuario recién creado
                            val id = usuarioViewModel.usuarioLogueadoId.value
                            if (id != null) {
                                // 2. Le avisa al transaccionViewModel quién es
                                transaccionViewModel.setUsuarioId(id)
                            }
                            currentScreen = MoneyFlowScreen.Home},
                        onLoginClick = { currentScreen = MoneyFlowScreen.Login }
                    )
                    MoneyFlowScreen.ForgotPassword -> ForgotPasswordScreen(
                        onBackClick = { currentScreen = MoneyFlowScreen.Login }
                    )

                    // Pasa transaccionViewModel para los números y usuarioViewModel para el nombre/sueldo
                    MoneyFlowScreen.Home -> HomeScreen(
                        transaccionViewModel = transaccionViewModel,
                        usuarioViewModel = usuarioViewModel,
                        onNavigate = { currentScreen = it }
                    )

                    // PASA LAS VARIABLES DEL SENSOR A LA PANTALLA DE AGREGAR GASTO
                    MoneyFlowScreen.AddExpense -> AddExpenseScreen(
                        viewModel = transaccionViewModel,
                        onNavigate = { currentScreen = it },
                        shakeTriggered = onShakeEvent.value, // <-- Le pasa el valor actual (true/false)
                        onPositionReset = { onShakeEvent.value = false } // <-- Compose avisa que ya limpió las cajas y resetea el sensor a false
                    )
                    MoneyFlowScreen.Savings -> SavingsScreen(
                        viewModel = metaAhorroViewModel,
                        usuarioId = usuarioViewModel.usuarioLogueadoId.collectAsState().value ?: 0,
                        onNavigate = { currentScreen = it }
                    )
                    // Pasa usuarioViewModel para ver y editar los datos del perfil
                    MoneyFlowScreen.Profile -> ProfileScreen(
                        viewModel = usuarioViewModel,
                        onNavigate = { currentScreen = it }
                    )

                    MoneyFlowScreen.NewSavingGoal -> NewSavingGoalScreen(
                        viewModel = metaAhorroViewModel,
                        usuarioId = usuarioViewModel.usuarioLogueadoId.collectAsState().value ?: 0,
                        onNavigate = { currentScreen = it }
                    )
                }
            }
        }
    }

    // --- MANEJO CRÍTICO DEL CICLO DE VIDA  ---

    // Activa el sensor cuando la app pasa al frente
    override fun onResume() {
        super.onResume()
        accelerometer?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    // Apaga el sensor si el usuario minimiza la app o bloquea el celu (Evita consumir RAM y batería)
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

            // 🚀 LA FÓRMULA CORRECTA: Calculamos la aceleración restando la gravedad de la Tierra (9.8)
            val aceleracionNeta = sqrt((x * x + y * y + z * z).toDouble()).toFloat() - SensorManager.GRAVITY_EARTH

            // Usamos el valor absoluto (Math.abs) para que no importe si el sacudón es hacia adelante o hacia atrás
            accelerationCurrent = accelerationCurrent * 0.9f + aceleracionNeta // Filtro básico para suavizar ruido
            val delta: Float = kotlin.math.abs(accelerationCurrent - accelerationLast)

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