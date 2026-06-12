package com.example.moneyflow.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moneyflow.navigation.MoneyFlowScreen

@Composable
fun BottomBar(
    selected: MoneyFlowScreen,
    onNavigate: (MoneyFlowScreen) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BottomItem("♡", "Home", selected == MoneyFlowScreen.Home) {
            onNavigate(MoneyFlowScreen.Home)
        }

        BottomItem("▣", "Gastos", selected == MoneyFlowScreen.AddExpense) {
            onNavigate(MoneyFlowScreen.AddExpense)
        }

        BottomItem("▯", "Ahorros", selected == MoneyFlowScreen.Savings) {
            onNavigate(MoneyFlowScreen.Savings)
        }

        BottomItem("☻", "Perfil", selected == MoneyFlowScreen.Profile) {
            onNavigate(MoneyFlowScreen.Profile)
        }
    }
}

@Composable
private fun BottomItem(
    icon: String,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier.clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = icon,
            fontSize = 24.sp,
            color = if (selected) Color(0xFF7154B8) else Color.Black
        )
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color.Black
        )
    }
}