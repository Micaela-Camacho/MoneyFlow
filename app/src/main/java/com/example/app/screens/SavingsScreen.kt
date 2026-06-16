package com.example.app.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app.MetaAhorroViewModel
import com.example.app.components.BottomBar
import com.example.app.navigation.MoneyFlowScreen
import java.util.Locale

@Composable
fun SavingsScreen(
    viewModel: MetaAhorroViewModel,
    usuarioId: Int,
    onNavigate: (MoneyFlowScreen) -> Unit
) {
    val metas by viewModel.obtenerMetasPorUsuario(usuarioId).collectAsState(initial = emptyList())

    val purple = Color(0xFF7154B8)
    val lightPurple = Color(0xFFE8DDFF)
    val veryLightPurple = Color(0xFFFFF7FF)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 28.dp, vertical = 48.dp)
    ) {
        IconButton(onClick = { onNavigate(MoneyFlowScreen.Home) }) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
        }

        Text(
            text = "Mis ahorros",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(30.dp))

        if (metas.isEmpty()) {
            Text(
                text = "Todavía no tenés metas creadas.",
                color = Color.Gray
            )
        } else {
            metas.forEach { meta ->
                val progreso = if (meta.montoObjetivo > 0) {
                    (meta.montoActual / meta.montoObjetivo).toFloat().coerceIn(0f, 1f)
                } else 0f

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = veryLightPurple),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Meta: ${meta.nombre}",
                                color = Color.Gray,
                                fontSize = 14.sp
                            )

                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "Más opciones",
                                tint = Color.Gray
                            )
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        Text(
                            text = "$${String.format(Locale("es", "AR"), "%,.0f", meta.montoActual)}",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "De $${String.format(Locale("es", "AR"), "%,.0f", meta.montoObjetivo)}",
                            color = Color.Gray,
                            fontSize = 14.sp
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        LinearProgressIndicator(
                            progress = progreso,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(10.dp)
                                .clip(RoundedCornerShape(50.dp)),
                            color = purple,
                            trackColor = lightPurple
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                onNavigate(MoneyFlowScreen.NewSavingGoal)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(containerColor = purple)
        ) {
            Text("Nueva meta", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(20.dp))

        BottomBar(
            selected = MoneyFlowScreen.Savings,
            onNavigate = onNavigate
        )
    }
}