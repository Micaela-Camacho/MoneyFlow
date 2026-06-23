package com.example.moneyflow.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moneyflow.TransaccionViewModel
import com.example.moneyflow.components.BottomBar
import com.example.moneyflow.navigation.MoneyFlowScreen
import java.util.Locale

@Composable
fun ExpensesScreen(
    viewModel: TransaccionViewModel,
    onNavigate: (MoneyFlowScreen) -> Unit
) {
    val transacciones by viewModel.transaccionesUsuario.collectAsState()

    val gastosReales = transacciones.filter { it.tipo == "EGRESO" }

    val gastosDemo = listOf(
        com.example.moneyflow.data.Transaccion(
            id = 1,
            usuarioId = 1,
            descripcion = "Compra mensual",
            monto = 15000.0,
            tipo = "EGRESO",
            categoria = "Supermercado",
            fecha = System.currentTimeMillis()
        ),
        com.example.moneyflow.data.Transaccion(
            id = 2,
            usuarioId = 1,
            descripcion = "SUBE",
            monto = 5000.0,
            tipo = "EGRESO",
            categoria = "Transporte",
            fecha = System.currentTimeMillis()
        ),
        com.example.moneyflow.data.Transaccion(
            id = 3,
            usuarioId = 1,
            descripcion = "Cena",
            monto = 10000.0,
            tipo = "EGRESO",
            categoria = "Comida",
            fecha = System.currentTimeMillis()
        ),
        com.example.moneyflow.data.Transaccion(
            id = 4,
            usuarioId = 1,
            descripcion = "Netflix",
            monto = 7000.0,
            tipo = "EGRESO",
            categoria = "Entretenimiento",
            fecha = System.currentTimeMillis()
        )
    )

    val gastos = if (gastosReales.isEmpty()) {
        gastosDemo
    } else {
        gastosReales
    }

    val totalGastos = gastos.sumOf { it.monto }

    val gastosPorCategoria = gastos
        .groupBy { it.categoria.ifBlank { "Otros" } }
        .mapValues { item -> item.value.sumOf { it.monto } }

    val purple = Color(0xFF7154B8)
    val veryLightPurple = Color(0xFFFFF7FF)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 28.dp, vertical = 48.dp)
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            IconButton(onClick = { onNavigate(MoneyFlowScreen.Home) }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
            }

            Text(
                text = "Mis gastos",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Resumen de gastos por categoría.",
                color = Color.Gray,
                fontSize = 13.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = veryLightPurple),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Total gastado",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )

                    Text(
                        text = "$${String.format(Locale("es", "AR"), "%,.0f", totalGastos)}",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(22.dp))

                    if (gastosPorCategoria.isEmpty()) {
                        Text(
                            text = "Todavía no hay gastos cargados.",
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    } else {
                        PieChart(
                            data = gastosPorCategoria,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(190.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        gastosPorCategoria.forEach { (categoria, monto) ->
                            Text(
                                text = "$categoria: $${String.format(Locale("es", "AR"), "%,.0f", monto)}",
                                fontSize = 14.sp,
                                color = Color.DarkGray
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            Button(
                onClick = { onNavigate(MoneyFlowScreen.AddExpense) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(containerColor = purple)
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Nuevo gasto", fontWeight = FontWeight.Bold)
            }
        }

        BottomBar(
            selected = MoneyFlowScreen.Expenses,
            onNavigate = onNavigate
        )
    }
}

@Composable
private fun PieChart(
    data: Map<String, Double>,
    modifier: Modifier = Modifier
) {
    val colors = listOf(
        Color(0xFF7154B8),
        Color(0xFFA792CC),
        Color(0xFFD1C4E9),
        Color(0xFF9575CD),
        Color(0xFFB39DDB),
        Color(0xFFE1BEE7),
        Color(0xFFCE93D8),
        Color(0xFFAB47BC)
    )

    val total = data.values.sum().toFloat()

    Canvas(modifier = modifier) {
        var startAngle = -90f
        val chartSize = size.minDimension

        data.values.forEachIndexed { index, value ->
            val sweepAngle = ((value / total) * 360f).toFloat()

            drawArc(
                color = colors[index % colors.size],
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = true,
                size = Size(chartSize, chartSize)
            )

            startAngle += sweepAngle
        }
    }
}