package com.example.moneyflow.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moneyflow.TransaccionViewModel
import com.example.moneyflow.components.BottomBar
import com.example.moneyflow.data.Transaccion
import com.example.moneyflow.navigation.MoneyFlowScreen
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HomeScreen(
    viewModel: TransaccionViewModel, //  Recibe el motor
    onNavigate: (MoneyFlowScreen) -> Unit
) {
    // ve la base de datos en tiempo real
    val listaTransacciones by viewModel.todasLasTransacciones.collectAsState(initial = emptyList())

    // procesa los montos según lo que haya en la base de datos
    val totalIngresos = listaTransacciones.filter { it.tipo == "INGRESO" }.sumOf { it.monto }
    val totalGastos = listaTransacciones.filter { it.tipo == "EGRESO" }.sumOf { it.monto }
    val saldoDisponible = totalIngresos - totalGastos
    val totalAhorros = 0.0 //

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFA792CC))
                .padding(18.dp)
        ) {
            Text(
                text = "Hola, Usuario 👋", //
                color = Color.White,
                fontSize = 20.sp
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(20.dp)
        ) {

            Text(
                text = "Resumen Actual",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            // CARD DE SALDO REAL
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF9B87C9)
                )
            ) {
                Column(
                    modifier = Modifier.padding(18.dp)
                ) {
                    Text(
                        text = "Saldo disponible",
                        color = Color.White
                    )

                    Text(
                        text = "$${String.format(Locale("es", "AR"), "%,.2f", saldoDisponible)}",
                        color = Color.White,
                        fontSize = 32.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // CARDS PEQUEÑAS CON TOTALES AUTOMÁTICOS
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                SmallCard(
                    "Gastos",
                    "$${String.format(Locale("es", "AR"), "%,.2f", totalGastos)}",
                    Color(0xFFFFE5E8),
                    Color.Red
                )

                SmallCard(
                    "Ahorros",
                    "$${String.format(Locale("es", "AR"), "%,.2f", totalAhorros)}",
                    Color(0xFF73CD8C),
                    Color(0xFF006B35)
                )

                SmallCard(
                    "Ingresos",
                    "$${String.format(Locale("es", "AR"), "%,.2f", totalIngresos)}",
                    Color(0xFF5AAAF5),
                    Color.Blue
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    onNavigate(MoneyFlowScreen.AddExpense)
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF7154B8)
                )
            ) {
                Text("Nuevo gasto")
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Últimos movimientos",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            // LISTA DINÁMICA: Si no hay movimientos muestra un aviso, si hay usa un LazyColumn
            if (listaTransacciones.isEmpty()) {
                Text(
                    text = "No hay movimientos registrados todavía.",
                    color = Color.Gray,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(vertical = 10.dp)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(listaTransacciones) { transaccion ->
                        // Formatea los milisegundos a una fecha legible
                        val formatoFecha = SimpleDateFormat("dd 'de' MMMM", Locale("es", "AR"))
                        val fechaLegible = formatoFecha.format(Date(transaccion.fecha))

                        // Cambia el signo y el color según corresponda
                        val esIngreso = transaccion.tipo == "INGRESO"
                        val signo = if (esIngreso) "+$" else "-$"
                        val colorMonto = if (esIngreso) Color(0xFF18B85A) else Color.Red

                        MovementItem(
                            title = transaccion.descripcion,
                            date = fechaLegible,
                            amount = "$signo${String.format(Locale("es", "AR"), "%,.2f", transaccion.monto)}",
                            amountColor = colorMonto
                        )
                    }
                }
            }
        }

        BottomBar(
            selected = MoneyFlowScreen.Home,
            onNavigate = onNavigate
        )
    }
}

@Composable
private fun SmallCard(
    title: String,
    amount: String,
    background: Color,
    textColor: Color
) {
    Card(
        modifier = Modifier
            .width(104.dp)
            .height(78.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = background
        )
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Text(
                text = title,
                fontSize = 12.sp,
                color = textColor
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = amount,
                fontSize = 11.sp,
                color = textColor
            )
        }
    }
}

@Composable
private fun MovementItem(
    title: String,
    date: String,
    amount: String,
    amountColor: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 7.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = title,
                fontSize = 16.sp
            )

            Text(
                text = date,
                fontSize = 12.sp,
                color = Color.Gray
            )
        }

        Text(
            text = amount,
            color = amountColor,
            fontSize = 14.sp
        )
    }
}