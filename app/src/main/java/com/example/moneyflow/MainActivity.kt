package com.example.moneyflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.example.moneyflow.navigation.MoneyFlowScreen
import com.example.moneyflow.screens.*
import com.example.moneyflow.ui.theme.MoneyFlowTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

                    MoneyFlowScreen.Home -> HomeScreen(
                        onNavigate = { currentScreen = it }
                    )

                    MoneyFlowScreen.AddExpense -> AddExpenseScreen(
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