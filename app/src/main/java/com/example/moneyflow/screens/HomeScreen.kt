package com.example.moneyflow.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moneyflow.TransaccionViewModel
import com.example.moneyflow.UsuarioViewModel
import com.example.moneyflow.components.BottomBar
import com.example.moneyflow.navigation.MoneyFlowScreen
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Estructura de datos simulada para los descuentos del JSON de Mica expandido
data class DescuentoMock(val comercio: String, val beneficio: String, val categoria: String,val distancia: String, val color: Color)

@Composable
fun HomeScreen(
    transaccionViewModel: TransaccionViewModel,
    usuarioViewModel: UsuarioViewModel,
    onNavigate: (MoneyFlowScreen) -> Unit
) {
    // 🔄 DISPARADOR ASÍNCRONO DE APIS
    val contexto = LocalContext.current
    LaunchedEffect(Unit) {
        transaccionViewModel.cargarDatosDeApis(contexto)
    }

    // 📡 ESCUCHADORES DE ESTADO (MÓDULO APIS Y FLUJOS)
    val precioOficial by transaccionViewModel.dolarOficial.collectAsState()
    val precioBlue by transaccionViewModel.dolarBlue.collectAsState()
    val listaDescuentosJson by transaccionViewModel.descuentosJson.collectAsState()

    // Estados nativos del usuario y transacciones
    val transacciones by transaccionViewModel.transaccionesUsuario.collectAsState()
    val usuarioActual by usuarioViewModel.usuarioActual.collectAsState()

    val totalGastos by transaccionViewModel.totalGastos.collectAsState()
    val totalIngresosExtra by transaccionViewModel.totalIngresosExtra.collectAsState()
    val totalAhorros by transaccionViewModel.totalAhorros.collectAsState()

    val sueldoBase = usuarioActual?.sueldoMensual ?: 0.0
    val nombreUsuario = usuarioActual?.nombre ?: "Usuario"
    val saldoDisponible = sueldoBase + totalIngresosExtra - totalGastos - totalAhorros

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Cabecera fija
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFA792CC))
                .padding(18.dp)
        ) {
            Text(
                text = "Hola, $nombreUsuario 👋",
                color = Color.White,
                fontSize = 20.sp
            )
        }

        // SECCIÓN CON SCROLL VERTICAL GENERAL (LazyColumn principal)
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 20.dp),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            // 1. COMPONENTE API DÓLAR REAL DINÁMICO
            item {
                Text(
                    text = "Cotización del Dólar",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(6.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF1EFF8))
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Dólar Oficial", fontSize = 12.sp, color = Color.DarkGray)
                            Text(text = precioOficial, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF006B35))
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Dólar Blue", fontSize = 12.sp, color = Color.DarkGray)
                            Text(text = precioBlue, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF006B35))
                        }
                    }
                }
            }

            // 2. CARD DE SALDO
            item {
                Text(
                    text = "Resumen Actual",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF9B87C9))
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(text = "Saldo disponible", color = Color.White)
                        Text(
                            text = "$${String.format(Locale("es", "AR"), "%,.2f", saldoDisponible)}",
                            color = Color.White,
                            fontSize = 32.sp
                        )
                    }
                }
            }

            // 3. MINICARDS (Gastos y Ahorros)
            item {
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
                }
            }

            // 4. SECCIÓN: DESCUENTOS DESTACADOS DINÁMICOS (Mica JSON Parseado con Distancia)
            item {
                Text(
                    text = "Club de Descuentos Beneficios",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(10.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(listaDescuentosJson) { desc ->
                        // Le subimos un pelín el alto a 115.dp para que entre el texto extra sin problemas
                        Card(
                            modifier = Modifier.width(160.dp).height(115.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = desc.color)
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(desc.categoria, fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.SemiBold)
                                    Text(desc.comercio, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                                }
                                Column {
                                    Text(desc.beneficio, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color(0xFF4A148C))
                                    // 📍 Pintamos la distancia consumida del Git abajo de todo en griscito:
                                    Text(desc.distancia, fontSize = 10.sp, color = Color(0xFF555555), fontWeight = FontWeight.Normal)
                                }
                            }
                        }
                    }
                }
            }

            // 5. MOVIMIENTOS
            item {
                Text(
                    text = "Últimos movimientos",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
            }

            if (transacciones.isEmpty()) {
                item {
                    Text(
                        text = "No hay movimientos registrados todavía.",
                        color = Color.Gray,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(vertical = 10.dp)
                    )
                }
            } else {
                items(transacciones) { transaccion ->
                    val formatoFecha = SimpleDateFormat("dd 'de' MMMM", Locale("es", "AR"))
                    val fechaLegible = formatoFecha.format(Date(transaccion.fecha))

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

        BottomBar(
            selected = MoneyFlowScreen.Home,
            onNavigate = onNavigate
        )
    }
}

@Composable
private fun SmallCard(title: String, amount: String, background: Color, textColor: Color) {
    Card(
        modifier = Modifier.width(140.dp).height(78.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = background)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(text = title, fontSize = 12.sp, color = textColor)
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = amount, fontSize = 11.sp, color = textColor)
        }
    }
}

@Composable
private fun MovementItem(title: String, date: String, amount: String, amountColor: Color) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 7.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(text = title, fontSize = 16.sp)
            Text(text = date, fontSize = 12.sp, color = Color.Gray)
        }
        Text(text = amount, color = amountColor, fontSize = 14.sp)
    }
}