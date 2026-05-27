package com.example.moneyflow.ui.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
// Panel/Dashboard principal de la app
fun DashboardScreen(onNavigateToAddGasto: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = "Resumen del Mes", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Sueldo Base: $450.000,00", style = MaterialTheme.typography.titleMedium)
                Text(text = "Gastos Registrados: $227.000,00", style = MaterialTheme.typography.bodyMedium)
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                Text(text = "Saldo Disponible: $223.000,00", style = MaterialTheme.typography.titleLarge)
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
        Text(text = "Gastos Recientes (Mock)", style = MaterialTheme.typography.titleSmall, modifier = Modifier.align(Alignment.Start))
        Spacer(modifier = Modifier.height(8.dp))

        Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
            Text(text = "🏠 Alquiler Depto: $180.000,00", modifier = Modifier.padding(12.dp))
        }
        Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
            Text(text = "🛒 Supermercado: $35.000,00", modifier = Modifier.padding(12.dp))
        }

        Spacer(modifier = Modifier.weight(1f))
        Button(onClick = onNavigateToAddGasto, modifier = Modifier.fillMaxWidth()) {
            Text(text = "+ Agregar Nuevo Gasto")
        }
    }
}