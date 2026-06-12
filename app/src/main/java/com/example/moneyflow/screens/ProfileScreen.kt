package com.example.moneyflow.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moneyflow.navigation.MoneyFlowScreen

@Composable
fun ProfileScreen(
    onNavigate: (MoneyFlowScreen) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(
                modifier = Modifier.size(72.dp),
                shape = CircleShape,
                color = Color(0xFFE8DDF8)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text("👤", fontSize = 34.sp)
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text("Nombre Usuario", fontSize = 20.sp)
                Text("usuario@usuario.com", color = Color.Gray)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Información financiera")
                Spacer(modifier = Modifier.height(12.dp))
                Text("Sueldo mensual", color = Color.Gray)
                Text("$600.000,00", fontSize = 24.sp)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text("Categorías  >")
        Spacer(modifier = Modifier.height(16.dp))
        Text("Metas de ahorro  >")
        Spacer(modifier = Modifier.height(16.dp))
        Text("Dark mode")
        Spacer(modifier = Modifier.height(16.dp))
        Text("Configuración  >")
        Spacer(modifier = Modifier.height(16.dp))
        Text("Cerrar sesión  >", color = Color.Red)

        Spacer(modifier = Modifier.weight(1f))

        TextButton(onClick = { onNavigate(MoneyFlowScreen.Home) }) {
            Text("← Volver")
        }
    }
}