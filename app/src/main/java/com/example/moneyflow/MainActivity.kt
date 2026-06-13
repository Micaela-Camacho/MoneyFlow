package com.example.moneyflow

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
        super.onCreate(savedInstanceState) //

        // 1. Conecta con la base de datos global de MoneyFlowApp
        val app = application as MoneyFlowApp
        val database = app.database

        // 2. Fabrica el ViewModel pasándole el DAO
        val factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return TransaccionViewModel(database.transaccionDao()) as T
            }
        }
        val viewModel = ViewModelProvider(this, factory)[TransaccionViewModel::class.java]

        setContent {
            MoneyFlowTheme {
                var currentScreen by remember { mutableStateOf<MoneyFlowScreen>(MoneyFlowScreen.Splash) }

                when (currentScreen) {
                    MoneyFlowScreen.Splash -> SplashScreen(
                        onStartClick = { currentScreen = MoneyFlowScreen.Login }
                    )

                    MoneyFlowScreen.Login -> LoginScreen(
                        onLoginClick = { currentScreen = MoneyFlowScreen.Home },
                        onRegisterClick = { currentScreen = MoneyFlowScreen.Register }
                    )

                    MoneyFlowScreen.Register -> RegisterScreen(
                        onBackClick = { currentScreen = MoneyFlowScreen.Login },
                        onCreateAccountClick = { currentScreen = MoneyFlowScreen.Home },
                        onLoginClick = { currentScreen = MoneyFlowScreen.Login }
                    )

                    // 3. Le pasa el viewModel a la Home para que dibuje la lista de gastos
                    MoneyFlowScreen.Home -> HomeScreen(
                        viewModel = viewModel,
                        onNavigate = { currentScreen = it }
                    )

                    // 4. Le pasa el viewModel a la pantalla de agregar para que guarde en la BD
                    MoneyFlowScreen.AddExpense -> AddExpenseScreen(
                        viewModel = viewModel,
                        onNavigate = { currentScreen = it }
                    )

                    MoneyFlowScreen.Savings -> SavingsScreen(
                        onNavigate = { currentScreen = it }
                    )

                    MoneyFlowScreen.Profile -> ProfileScreen(
                        onNavigate = { currentScreen = it }
                    )
                }
            }
        }
    }
}