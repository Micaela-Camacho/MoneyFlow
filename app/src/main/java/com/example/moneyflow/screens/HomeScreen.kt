package com.example.moneyflow.screens
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
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

data class DescuentoMock(
    val comercio: String,
    val beneficio: String,
    val categoria: String,
    val distancia: String,
    val color: Color
)

@Composable
fun HomeScreen(
    transaccionViewModel: TransaccionViewModel,
    usuarioViewModel: UsuarioViewModel,
    onNavigate: (MoneyFlowScreen) -> Unit
) {
    val contexto = LocalContext.current

    LaunchedEffect(Unit) {
        transaccionViewModel.cargarDatosDeApis(contexto)
    }

    val precioOficial by transaccionViewModel.dolarOficial.collectAsState()
    val precioBlue by transaccionViewModel.dolarBlue.collectAsState()
    val listaDescuentosJson by transaccionViewModel.descuentosJson.collectAsState()

    val transacciones by transaccionViewModel.transaccionesUsuario.collectAsState()
    val usuarioActual by usuarioViewModel.usuarioActual.collectAsState()

    val totalGastos by transaccionViewModel.totalGastos.collectAsState()
    val totalIngresosExtra by transaccionViewModel.totalIngresosExtra.collectAsState()
    val totalAhorros by transaccionViewModel.totalAhorros.collectAsState()

    val sueldoBase = usuarioActual?.sueldoMensual ?: 0.0
    val nombreUsuario = usuarioActual?.nombre ?: "Usuario"
    val saldoDisponible = sueldoBase + totalIngresosExtra - totalGastos - totalAhorros
    var mostrarSaldo by remember { mutableStateOf(true) }
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
                text = "Hola, $nombreUsuario 👋",
                color = Color.White,
                fontSize = 20.sp
            )
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 20.dp),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
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
                        modifier = Modifier
                            .padding(12.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Dólar Oficial", fontSize = 12.sp, color = Color.DarkGray)
                            Text(
                                text = precioOficial,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF006B35)
                            )
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Dólar Blue", fontSize = 12.sp, color = Color.DarkGray)
                            Text(
                                text = precioBlue,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF006B35)
                            )
                        }
                    }
                }
            }

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
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF9B87C9)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Column {
                            Text(
                                text = "Saldo disponible",
                                color = Color.White
                            )

                            Text(
                                text = if (mostrarSaldo)
                                    "$${String.format(Locale("es", "AR"), "%,.2f", saldoDisponible)}"
                                else
                                    "••••••••••",
                                color = Color.White,
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        IconButton(
                            onClick = {
                                mostrarSaldo = !mostrarSaldo
                            }
                        ) {
                            Icon(
                                imageVector =
                                if (mostrarSaldo)
                                    Icons.Default.Visibility
                                else
                                    Icons.Default.VisibilityOff,
                                contentDescription = "Mostrar/Ocultar saldo",
                                tint = Color.White
                            )
                        }
                    }
                }
            }


            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    SmallCard(
                        modifier = Modifier.weight(1f),
                        title = "Gastos",
                        amount = "$${String.format(Locale("es", "AR"), "%,.2f", totalGastos)}",
                        background = Color(0xFFFFE5E8),
                        textColor = Color.Red
                    )

                    SmallCard(
                        modifier = Modifier.weight(1f),
                        title = "Ahorros",
                        amount = "$${String.format(Locale("es", "AR"), "%,.2f", totalAhorros)}",
                        background = Color(0xFF73CD8C),
                        textColor = Color(0xFF006B35)
                    )
                }
            }

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
                        Card(
                            modifier = Modifier
                                .width(160.dp)
                                .height(115.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = desc.color)
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(
                                        text = desc.categoria,
                                        fontSize = 10.sp,
                                        color = Color.Gray,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Text(
                                        text = desc.comercio,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Black
                                    )
                                }

                                Column {
                                    Text(
                                        text = desc.beneficio,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color(0xFF4A148C)
                                    )
                                    Text(
                                        text = desc.distancia,
                                        fontSize = 10.sp,
                                        color = Color(0xFF555555)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            item {
                Text(
                    text = "Últimos movimientos",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
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
private fun SmallCard(
    modifier: Modifier = Modifier,
    title: String,
    amount: String,
    background: Color,
    textColor: Color
) {
    Card(
        modifier = modifier.height(110.dp),
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = background
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                fontSize = 15.sp,
                color = textColor
            )

            Text(
                text = amount,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
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
            Text(text = title, fontSize = 16.sp)
            Text(text = date, fontSize = 12.sp, color = Color.Gray)
        }

        Text(
            text = amount,
            color = amountColor,
            fontSize = 14.sp
        )
    }
}