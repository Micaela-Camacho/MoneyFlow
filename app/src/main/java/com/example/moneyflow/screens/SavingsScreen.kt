package com.example.moneyflow.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moneyflow.navigation.MoneyFlowScreen

@Composable
fun SavingsScreen(
    onNavigate: (MoneyFlowScreen) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {

        TextButton(
            onClick = {
                onNavigate(MoneyFlowScreen.Home)
            }
        ) {
            Text("← Volver")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Mis ahorros",
            fontSize = 28.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Meta: Vacaciones")
                Spacer(modifier = Modifier.height(8.dp))
                Text("$320.000")
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = 0.55f,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("20 de mayo  +$5.000")
        Text("18 de mayo  +$15.000")
        Text("5 de mayo   +$40.000")
        Text("28 de abril +$10.000")
    }
}