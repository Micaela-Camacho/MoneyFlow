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

class MainActivity : ComponentActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null

    private var accelerationCurrent = SensorManager.GRAVITY_EARTH
    private var accelerationLast = SensorManager.GRAVITY_EARTH
    private var shakeThreshold = 2.5f

    private var onShakeEvent = mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        val app = application as MoneyFlowApp
        val database = app.database

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

        val transaccionViewModel = ViewModelProvider(this, factory)[TransaccionViewModel::class.java]
        val usuarioViewModel = ViewModelProvider(this, factory)[UsuarioViewModel::class.java]
        val metaAhorroViewModel = ViewModelProvider(this, factory)[MetaAhorroViewModel::class.java]

        setContent {
            MoneyFlowTheme {
                val prefs = remember { getSharedPreferences("MoneyFlowPrefs", Context.MODE_PRIVATE) }
                val yaVioOnboarding = remember { prefs.getBoolean("onboarding_completo", false) }

                var currentScreen by remember {
                    mutableStateOf<MoneyFlowScreen>(
                        if (yaVioOnboarding) MoneyFlowScreen.Login else MoneyFlowScreen.Splash
                    )
                }

                when (currentScreen) {
                    MoneyFlowScreen.Splash -> SplashScreen(
                        onStartClick = {
                            prefs.edit().putBoolean("onboarding_completo", true).apply()
                            currentScreen = MoneyFlowScreen.Login
                        }
                    )

                    MoneyFlowScreen.Login -> LoginScreen(
                        viewModel = usuarioViewModel,
                        onLoginClick = {
                            val id = usuarioViewModel.usuarioLogueadoId.value
                            if (id != null) {
                                transaccionViewModel.setUsuarioId(id)
                            }
                            currentScreen = MoneyFlowScreen.Home
                        },
                        onRegisterClick = { currentScreen = MoneyFlowScreen.Register },
                        onForgotPasswordClick = { currentScreen = MoneyFlowScreen.ForgotPassword }
                    )

                    MoneyFlowScreen.Register -> RegisterScreen(
                        viewModel = usuarioViewModel,
                        onBackClick = { currentScreen = MoneyFlowScreen.Login },
                        onCreateAccountClick = {
                            val id = usuarioViewModel.usuarioLogueadoId.value
                            if (id != null) {
                                transaccionViewModel.setUsuarioId(id)
                            }
                            currentScreen = MoneyFlowScreen.Home
                        },
                        onLoginClick = { currentScreen = MoneyFlowScreen.Login }
                    )

                    MoneyFlowScreen.ForgotPassword -> ForgotPasswordScreen(
                        onBackClick = { currentScreen = MoneyFlowScreen.Login }
                    )

                    MoneyFlowScreen.Home -> HomeScreen(
                        transaccionViewModel = transaccionViewModel,
                        usuarioViewModel = usuarioViewModel,
                        onNavigate = { currentScreen = it }
                    )

                    MoneyFlowScreen.Expenses -> ExpensesScreen(
                        viewModel = transaccionViewModel,
                        onNavigate = { currentScreen = it }
                    )

                    MoneyFlowScreen.AddExpense -> AddExpenseScreen(
                        viewModel = transaccionViewModel,
                        onNavigate = { currentScreen = it },
                        shakeTriggered = onShakeEvent.value,
                        onPositionReset = { onShakeEvent.value = false }
                    )

                    MoneyFlowScreen.Savings -> SavingsScreen(
                        viewModel = metaAhorroViewModel,
                        usuarioId = usuarioViewModel.usuarioLogueadoId.collectAsState().value ?: 0,
                        onNavigate = { currentScreen = it }
                    )

                    MoneyFlowScreen.Profile -> ProfileScreen(
                        viewModel = usuarioViewModel,
                        onNavigate = { currentScreen = it }
                    )

                    MoneyFlowScreen.NewSavingGoal -> NewSavingGoalScreen(
                        viewModel = metaAhorroViewModel,
                        usuarioId = usuarioViewModel.usuarioLogueadoId.collectAsState().value ?: 0,
                        onNavigate = { currentScreen = it }
                    )

                    MoneyFlowScreen.EditSavingGoal -> {
                        val metaId = metaAhorroViewModel.metaSeleccionadaId

                        if (metaId != null) {
                            EditSavingGoalScreen(
                                viewModel = metaAhorroViewModel,
                                metaId = metaId,
                                onNavigate = { currentScreen = it }
                            )
                        } else {
                            currentScreen = MoneyFlowScreen.Savings
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        accelerometer?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null && event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            accelerationLast = accelerationCurrent

            val aceleracionNeta =
                sqrt((x * x + y * y + z * z).toDouble()).toFloat() - SensorManager.GRAVITY_EARTH

            accelerationCurrent = accelerationCurrent * 0.9f + aceleracionNeta
            val delta = kotlin.math.abs(accelerationCurrent - accelerationLast)

            if (delta > shakeThreshold) {
                onShakeEvent.value = true
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}