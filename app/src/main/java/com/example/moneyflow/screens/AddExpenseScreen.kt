package com.example.moneyflow.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moneyflow.TransaccionViewModel
import com.example.moneyflow.components.BottomBar
import com.example.moneyflow.navigation.MoneyFlowScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseScreen(
    viewModel: TransaccionViewModel,
    onNavigate: (MoneyFlowScreen) -> Unit,
    shakeTriggered: Boolean,
    onPositionReset: () -> Unit
) {
    var amount by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    var expandedCategory by remember { mutableStateOf(false) }

    val categorias = listOf(
        "Supermercado",
        "Transporte",
        "Comida",
        "Servicios",
        "Salud",
        "Ropa",
        "Entretenimiento",
        "Otros"
    )

    val purple = Color(0xFF7154B8)

    LaunchedEffect(shakeTriggered) {
        if (shakeTriggered) {
            amount = ""
            category = ""
            description = ""
            onPositionReset()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 28.dp, vertical = 48.dp)
    ) {
        IconButton(onClick = { onNavigate(MoneyFlowScreen.Expenses) }) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Volver"
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Nuevo gasto",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Completá los datos del gasto.",
            color = Color.Gray,
            fontSize = 13.sp
        )

        Spacer(modifier = Modifier.height(34.dp))

        Text("Monto", fontSize = 16.sp)

        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            placeholder = { Text("Ej:1500") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Monto"
                )
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(14.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text("Categoría", fontSize = 16.sp)

        ExposedDropdownMenuBox(
            expanded = expandedCategory,
            onExpandedChange = {
                expandedCategory = !expandedCategory
            }
        ) {
            OutlinedTextField(
                value = category,
                onValueChange = {},
                readOnly = true,
                placeholder = { Text("Seleccionar categoría") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Categoría"
                    )
                },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = expandedCategory
                    )
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(14.dp)
            )

            ExposedDropdownMenu(
                expanded = expandedCategory,
                onDismissRequest = {
                    expandedCategory = false
                }
            ) {
                categorias.forEach { opcion ->
                    DropdownMenuItem(
                        text = { Text(opcion) },
                        onClick = {
                            category = opcion
                            expandedCategory = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text("Descripción (opcional)", fontSize = 16.sp)

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            placeholder = { Text("Ej: Almuerzo con amigos") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Descripción"
                )
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(14.dp)
        )

        Spacer(modifier = Modifier.height(52.dp))

        Button(
            onClick = {
                val montoNumerico = amount.toDoubleOrNull() ?: 0.0
                val descripcionFinal = description.ifBlank { category.ifBlank { "Gasto general" } }

                if (montoNumerico > 0.0) {
                    viewModel.agregarTransaccion(
                        usuarioId = 1,
                        descripcion = descripcionFinal,
                        monto = montoNumerico,
                        tipo = "EGRESO",
                        categoria = category.ifBlank { "Varios" }
                    )

                    onNavigate(MoneyFlowScreen.Expenses)
                }
            },
            enabled = amount.isNotBlank(),
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = purple
            )
        ) {
            Text(
                text = "Guardar gasto",
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        BottomBar(
            selected = MoneyFlowScreen.AddExpense,
            onNavigate = onNavigate
        )
    }
}