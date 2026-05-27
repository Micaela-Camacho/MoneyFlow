package com.example.moneyflow.ui.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AddGastoScreen(onNavigateBack: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = "Nuevo Gasto", style = MaterialTheme.typography.headlineMedium)

        // CAMPOS DE TEXTO DE DISEÑO (FORMULARIO MOCK)
        // enabled = false y valores fijos para que se renderice visualmente el formulario hasta tener la lógica reactiva
        OutlinedTextField(value = "Internet Fibra", onValueChange = {}, label = { Text("Descripción") }, enabled = false, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = "8500", onValueChange = {}, label = { Text("Monto ($)") }, enabled = false, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = "Servicios", onValueChange = {}, label = { Text("Categoría") }, enabled = false, modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.weight(1f))
        Button(onClick = onNavigateBack, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Guardar Gasto (Volver)")
        }
    }
}