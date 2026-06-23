package com.example.moneyflow.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moneyflow.MetaAhorroViewModel
import com.example.moneyflow.navigation.MoneyFlowScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewSavingGoalScreen(
    viewModel: MetaAhorroViewModel,
    usuarioId: Int,
    onNavigate: (MoneyFlowScreen) -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var montoObjetivo by remember { mutableStateOf("") }
    var montoActual by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val purple = Color(0xFF7154B8)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 28.dp, vertical = 48.dp)
    ) {
        IconButton(onClick = { onNavigate(MoneyFlowScreen.Savings) }) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
        }

        Text(
            text = "Nueva meta",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Creá una meta de ahorro.",
            color = Color.Gray,
            fontSize = 13.sp
        )

        Spacer(modifier = Modifier.height(34.dp))

        errorMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                fontSize = 13.sp
            )

            Spacer(modifier = Modifier.height(8.dp))
        }

        Text("Nombre de la meta")

        OutlinedTextField(
            value = nombre,
            onValueChange = {
                nombre = it
                errorMessage = null
            },
            placeholder = { Text("Ej: Vacaciones") },
            leadingIcon = {
                Icon(Icons.Default.Star, contentDescription = "Meta")
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(14.dp)
        )

        Spacer(modifier = Modifier.height(14.dp))

        Text("Monto objetivo")

        OutlinedTextField(
            value = montoObjetivo,
            onValueChange = {
                montoObjetivo = it
                errorMessage = null
            },
            placeholder = { Text("Ej: 600000") },
            leadingIcon = {
                Icon(Icons.Default.Star, contentDescription = "Monto objetivo")
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(14.dp)
        )

        Spacer(modifier = Modifier.height(14.dp))

        Text("Monto inicial")

        OutlinedTextField(
            value = montoActual,
            onValueChange = {
                montoActual = it
                errorMessage = null
            },
            placeholder = { Text("Ej: 320000") },
            leadingIcon = {
                Icon(Icons.Default.Star, contentDescription = "Monto inicial")
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(14.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                val objetivo = montoObjetivo.toDoubleOrNull()
                val actual = montoActual.toDoubleOrNull() ?: 0.0

                when {
                    nombre.isBlank() || montoObjetivo.isBlank() -> {
                        errorMessage = "Completá el nombre y el monto objetivo."
                    }

                    objetivo == null || objetivo <= 0 -> {
                        errorMessage = "Ingresá un monto objetivo válido."
                    }

                    actual < 0 -> {
                        errorMessage = "El monto inicial no puede ser negativo."
                    }

                    else -> {
                        viewModel.crearMeta(
                            usuarioId = usuarioId,
                            nombre = nombre,
                            montoObjetivo = objetivo,
                            montoActual = actual
                        )

                        onNavigate(MoneyFlowScreen.Savings)
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(containerColor = purple)
        ) {
            Text("Crear meta", fontWeight = FontWeight.Bold)
        }
    }
}