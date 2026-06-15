package com.example.moneyflow.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.moneyflow.TransaccionViewModel
import com.example.moneyflow.components.BottomBar
import com.example.moneyflow.navigation.MoneyFlowScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseScreen(
    viewModel: TransaccionViewModel, // recibe el motor de datos
    onNavigate: (MoneyFlowScreen) -> Unit,
    shakeTriggered: Boolean,         // 1. NUEVO: Recibe el aviso del sensor desde el Main
    onPositionReset: () -> Unit      // 2. NUEVO: Función para avisarle al Main que ya limpiamos
) {
    var amount by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    // 3. NUEVO: El "Efecto Lanzado" reacciona de inmediato cuando shakeTriggered pasa a ser TRUE
    LaunchedEffect(shakeTriggered) {
        if (shakeTriggered) {
            // Se vacían de forma limpia los estados de los TextField que hicieron las chicas
            amount = ""
            category = ""
            description = ""
            // Le avisamos a la MainActivity que ponga el gatillo en false otra vez
            onPositionReset()
        }
    }

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
            placeholder = { Text("Seleccionar categoría") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text("Descripción")
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            placeholder = { Text("Ej: Almuerzo con amigos") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = {
                // convierte el texto del monto a número de forma segura
                val montoNumerico = amount.toDoubleOrNull() ?: 0.0

                // Si la descripción está vacía, le pone el nombre de la categoría por defecto
                val descripcionFinal = description.ifBlank { category.ifBlank { "Gasto general" } }

                if (montoNumerico > 0.0) {
                    // Guarda físicamente en Room a través del ViewModel
                    viewModel.agregarTransaccion(
                        descripcion = descripcionFinal,
                        monto = montoNumerico,
                        tipo = "EGRESO", // Queda como egreso automático
                        categoria = category.ifBlank { "Varios" }
                    )
                    // Vuelve a la pantalla principal
                    onNavigate(MoneyFlowScreen.Home)
                }
            },
            enabled = amount.isNotBlank(), // El botón se deshabilita si no escribieron un monto
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