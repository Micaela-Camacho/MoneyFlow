package com.example.moneyflow
import com.example.moneyflow.MetaAhorroViewModel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.moneyflow.navigation.MoneyFlowScreen
import com.example.moneyflow.screens.*
import com.example.moneyflow.ui.theme.MoneyFlowTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Conecta con la base de datos global utilizando  Application class
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
                // Estado local para saber qué pantalla dibujar (arranca en la Splash Screen)
                var currentScreen by remember { mutableStateOf<MoneyFlowScreen>(MoneyFlowScreen.Splash) }

                when (currentScreen) {
                    MoneyFlowScreen.Splash -> SplashScreen(
                        onStartClick = { currentScreen = MoneyFlowScreen.Login }
                    )

                    // El Login ahora podría usar el usuarioViewModel más adelante para validar
                    MoneyFlowScreen.Login -> LoginScreen(
                        viewModel = usuarioViewModel,
                        onLoginClick = { currentScreen = MoneyFlowScreen.Home },
                        onRegisterClick = { currentScreen = MoneyFlowScreen.Register },
                        onForgotPasswordClick = { currentScreen = MoneyFlowScreen.ForgotPassword }
                    )
                    // El Registro va a usar el usuarioViewModel para insertar el nuevo usuario
                    MoneyFlowScreen.Register -> RegisterScreen(
                        viewModel = usuarioViewModel,
                        onBackClick = { currentScreen = MoneyFlowScreen.Login },
                        onCreateAccountClick = { currentScreen = MoneyFlowScreen.Home },
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

                    // Pasa el transaccionViewModel para que guarde los gastos
                    MoneyFlowScreen.AddExpense -> AddExpenseScreen(
                        viewModel = transaccionViewModel,
                        usuarioId = usuarioViewModel.usuarioLogueadoId.collectAsState().value ?: 0,
                        onNavigate = { currentScreen = it }
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
}