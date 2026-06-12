package com.example.moneyflow.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moneyflow.components.BottomBar
import com.example.moneyflow.navigation.MoneyFlowScreen

@Composable
fun HomeScreen(
    onNavigate: (MoneyFlowScreen) -> Unit
) {
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
                text = "Hola, Usuario 👋",
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
                text = "Resumen de Mayo⌄",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

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
                        text = "$450.000,00",
                        color = Color.White,
                        fontSize = 32.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {

                SmallCard(
                    "Gastos",
                    "$120.000,00",
                    Color(0xFFFFE5E8),
                    Color.Red
                )

                SmallCard(
                    "Ahorros",
                    "$80.000,00",
                    Color(0xFF73CD8C),
                    Color(0xFF006B35)
                )

                SmallCard(
                    "Ingresos",
                    "$600.000,00",
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

            MovementItem("Supermercado", "24 de mayo", "-$25.000,00", Color.Red)
            MovementItem("Transporte", "24 de mayo", "-$5.000,00", Color.Red)
            MovementItem("Sueldo", "24 de mayo", "+$600.000,00", Color(0xFF18B85A))
            MovementItem("Restaurante", "23 de mayo", "-$25.000,00", Color.Red)
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