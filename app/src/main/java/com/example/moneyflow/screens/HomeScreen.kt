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
import com.example.moneyflow.UsuarioViewModel
import com.example.moneyflow.components.BottomBar
import com.example.moneyflow.data.Transaccion
import com.example.moneyflow.navigation.MoneyFlowScreen
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HomeScreen(
    transaccionViewModel: TransaccionViewModel, // Recibe el motor de datos
    usuarioViewModel: UsuarioViewModel,         // Recibe el motor de usuarios
    onNavigate: (MoneyFlowScreen) -> Unit
) {
    // 1. Escucha las transacciones y el usuario en tiempo real
    val transacciones by transaccionViewModel.transaccionesUsuario.collectAsState()
    val usuarioActual by usuarioViewModel.usuarioActual.collectAsState()

    // 2. Traem los totales ya masticados y procesados desde el ViewModel
    val totalGastos by transaccionViewModel.totalGastos.collectAsState()
    val totalIngresosExtra by transaccionViewModel.totalIngresosExtra.collectAsState()
    val totalAhorros by transaccionViewModel.totalAhorros.collectAsState()

    // 3. LÓGICA DE PERFIL: Obtiene los datos base de la cuenta
    val sueldoBase = usuarioActual?.sueldoMensual ?: 0.0
    val nombreUsuario = usuarioActual?.nombre ?: "Usuario"

    // 4. El saldo final contempla de forma exacta el sueldo base, ingresos extra, gastos y ahorros
    val saldoDisponible = sueldoBase + totalIngresosExtra - totalGastos - totalAhorros

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
                text = "Hola, $nombreUsuario 👋", //
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
                )}





                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Últimos movimientos",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))

                // LISTA DINÁMICA: Si no hay movimientos muestra un aviso, si hay usa un LazyColumn
                if (transacciones.isEmpty()) {
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
                        items(transacciones) { transaccion ->
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
                                amount = "$signo${
                                    String.format(
                                        Locale("es", "AR"),
                                        "%,.2f",
                                        transaccion.monto
                                    )
                                }",
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
