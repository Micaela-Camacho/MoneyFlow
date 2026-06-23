package com.example.moneyflow.screens
import androidx.compose.material3.ExperimentalMaterial3Api

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moneyflow.MetaAhorroViewModel
import com.example.moneyflow.data.MetaAhorro
import com.example.moneyflow.navigation.MoneyFlowScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditSavingGoalScreen(
    viewModel: MetaAhorroViewModel,
    metaId: Int,
    onNavigate: (MoneyFlowScreen) -> Unit
) {
    val meta by viewModel.obtenerMetaPorId(metaId).collectAsState(initial = null)

    var nombre by remember { mutableStateOf("") }
    var montoObjetivo by remember { mutableStateOf("") }
    var montoActual by remember { mutableStateOf("") }

    LaunchedEffect(meta) {
        meta?.let {
            nombre = it.nombre
            montoObjetivo = it.montoObjetivo.toString()
            montoActual = it.montoActual.toString()
        }
    }

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
            text = "Editar meta",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(30.dp))

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre de la meta") },
            leadingIcon = {
                Icon(Icons.Default.TrackChanges, contentDescription = null)
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = montoObjetivo,
            onValueChange = { montoObjetivo = it },
            label = { Text("Monto objetivo") },
            leadingIcon = {
                Icon(Icons.Default.Savings, contentDescription = null)
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = montoActual,
            onValueChange = { montoActual = it },
            label = { Text("Monto actual") },
            leadingIcon = {
                Icon(Icons.Default.Savings, contentDescription = null)
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                val metaActual = meta

                if (metaActual != null) {
                    val metaEditada = MetaAhorro(
                        id = metaActual.id,
                        usuarioId = metaActual.usuarioId,
                        nombre = nombre,
                        montoObjetivo = montoObjetivo.toDoubleOrNull() ?: 0.0,
                        montoActual = montoActual.toDoubleOrNull() ?: 0.0,
                        fechaCreacion = metaActual.fechaCreacion
                    )

                    viewModel.actualizarMeta(metaEditada)
                    onNavigate(MoneyFlowScreen.Savings)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(containerColor = purple)
        ) {
            Text("Guardar cambios", fontWeight = FontWeight.Bold)
        }
    }
}