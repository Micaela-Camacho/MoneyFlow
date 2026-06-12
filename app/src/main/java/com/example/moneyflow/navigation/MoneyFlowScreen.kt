package com.example.moneyflow.navigation

sealed class MoneyFlowScreen {

    object Splash : MoneyFlowScreen()
    object Login : MoneyFlowScreen()
    object Register : MoneyFlowScreen()
    object Home : MoneyFlowScreen()
    object AddExpense : MoneyFlowScreen()
    object Savings : MoneyFlowScreen()
    object Profile : MoneyFlowScreen()
}