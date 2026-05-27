package com.example.moneyflow.ui.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LoginScreen(onNavigateToDashboard: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = "MoneyFlow", style = MaterialTheme.typography.headlineLarge)
        Text(text = "Organizá tus finanzas, alcanzá tus metas", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(30.dp))
        Button(onClick = onNavigateToDashboard) {
            Text(text = "Ingresar a la App")
        }
    }
}