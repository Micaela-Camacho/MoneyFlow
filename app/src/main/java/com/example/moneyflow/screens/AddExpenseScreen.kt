package com.example.moneyflow.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.moneyflow.components.BottomBar
import com.example.moneyflow.navigation.MoneyFlowScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseScreen(
    onNavigate: (MoneyFlowScreen) -> Unit
) {
    var amount by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp)
    ) {
        TextButton(onClick = { onNavigate(MoneyFlowScreen.Home) }) {
            Text("←", color = Color.Black)
        }

        Text(
            text = "Nuevo gasto",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "Completá los datos del gasto.",
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(36.dp))

        Text("Monto")
        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            placeholder = { Text("$  Ej:1500") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text("Categoría")
        OutlinedTextField(
            value = category,
            onValueChange = { category = it },
            placeholder = { Text("☺  Seleccionar categoría") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text("Fecha")
        OutlinedTextField(
            value = date,
            onValueChange = { date = it },
            placeholder = { Text("MM/DD/YYYY") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text("Descripción (opcional)")
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            placeholder = { Text("▤  Ej: Almuerzo con amigos") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = { onNavigate(MoneyFlowScreen.Home) },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF7154B8)
            )
        ) {
            Text("Guardar gasto")
        }

        Spacer(modifier = Modifier.weight(1f))

        BottomBar(
            selected = MoneyFlowScreen.AddExpense,
            onNavigate = onNavigate
        )
    }
}