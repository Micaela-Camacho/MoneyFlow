package com.example.moneyflow.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moneyflow.UsuarioViewModel
import com.example.moneyflow.navigation.MoneyFlowScreen

@Composable
fun ProfileScreen(
    viewModel: UsuarioViewModel, // Recibe el controlador de usuarios desde MainActivity
    onNavigate: (MoneyFlowScreen) -> Unit
) {
    // 1. Escucha al usuario logueado en tiempo real
    val usuarioActual by viewModel.usuarioActual.collectAsState()

    // 2. Variables dinámicas (si no hay nadie logueado por error, muestra un texto por defecto)
    val nombreMostrar = usuarioActual?.nombre ?: "Nombre Usuario"
    val emailMostrar = usuarioActual?.email ?: "usuario@usuario.com"
    val sueldoMostrar = usuarioActual?.sueldoMensual ?: 0.0

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
                // 3. DATOS REALES DE LA BASE DE DATOS
                Text(text = nombreMostrar, fontSize = 20.sp)
                Text(text = emailMostrar, color = Color.Gray)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Información financiera")
                Spacer(modifier = Modifier.height(12.dp))
                Text("Sueldo mensual", color = Color.Gray)
                // 4. Formateamos el sueldo real del usuario con formato regional de Argentina
                Text(
                    text = "$${String.format(java.util.Locale("es", "AR"), "%,.2f", sueldoMostrar)}",
                    fontSize = 24.sp
                )
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
        // 5. Lógica para el botón de Cerrar Sesión
        TextButton(
            onClick = {
                viewModel.cerrarSesion() // Borra el usuario de la memoria
                onNavigate(MoneyFlowScreen.Login) // Lo manda al Login de nuevo
            },
            contentPadding = PaddingValues(0.dp)
        ) {
            Text("Cerrar sesión  >", color = Color.Red)
        }

        Spacer(modifier = Modifier.weight(1f))

        TextButton(onClick = { onNavigate(MoneyFlowScreen.Home) }) {
            Text("← Volver")
        }
    }
}