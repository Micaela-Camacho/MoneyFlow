package com.example.moneyflow.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moneyflow.UsuarioViewModel
import com.example.moneyflow.components.BottomBar
import com.example.moneyflow.navigation.MoneyFlowScreen

@Composable
fun ProfileScreen(
    viewModel: UsuarioViewModel,
    onNavigate: (MoneyFlowScreen) -> Unit
) {
    // 1. Escucha al usuario logueado en tiempo real
    val usuarioActual by viewModel.usuarioActual.collectAsState()
    val usuarioId by viewModel.usuarioLogueadoId.collectAsState()
    // Reacción automática al cerrar sesión
    LaunchedEffect(usuarioId) {
        if (usuarioId == null) {
            onNavigate(MoneyFlowScreen.Login)
        }
    }
    // 2. Variables dinámicas (si no hay nadie logueado por error, muestra un texto por defecto)
    val nombreMostrar = usuarioActual?.nombre ?: "Nombre Usuario"
    val emailMostrar = usuarioActual?.email ?: "usuario@usuario.com"
    val sueldoMostrar = usuarioActual?.sueldoMensual ?: 0.0

    // Estado visual del switch de Dark Mode
    var darkMode by remember { mutableStateOf(false) }

    val purple = Color(0xFF7154B8)
    val lightPurple = Color(0xFFE8DDFF)
    val veryLightPurple = Color(0xFFF6F1FA)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 24.dp, vertical = 40.dp)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(88.dp)
                    .background(lightPurple, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Perfil",
                    tint = purple,
                    modifier = Modifier.size(68.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                // 3. DATOS REALES DE LA BASE DE DATOS
                Text(
                    text = nombreMostrar,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = emailMostrar,
                    color = Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = veryLightPurple
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Información financiera",
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Sueldo mensual",
                                color = Color.Gray,
                                fontSize = 13.sp
                            )

                            // 4. Formatea el sueldo real del usuario con formato regional de Argentina
                            Text(
                                text = "$${String.format(java.util.Locale("es", "AR"), "%,.2f", sueldoMostrar)}",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Editar sueldo"
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(36.dp))

        ProfileOptionRow(
            icon = Icons.Default.Star,
            text = "Categorías"
        )

        ProfileOptionRow(
            icon = Icons.Default.Star,
            text = "Metas de ahorro",
            onClick = {
                onNavigate(MoneyFlowScreen.Savings)
            }
        )

        ProfileOptionRow(
            icon = Icons.Default.Settings,
            text = "Dark mode",
            showSwitch = true,
            switchChecked = darkMode,
            onSwitchChange = {
                darkMode = it
            }
        )

        ProfileOptionRow(
            icon = Icons.Default.Settings,
            text = "Configuración"
        )

        ProfileOptionRow(
            icon = Icons.Default.AccountCircle,
            text = "Cerrar sesión",
            textColor = Color.Red,
            iconColor = Color.Red,
            arrowColor = Color.Red,
            onClick = {
                // 5. Lógica para el botón de Cerrar Sesión
                viewModel.cerrarSesion()
            }
        )

        Spacer(modifier = Modifier.weight(1f))

        BottomBar(
            selected = MoneyFlowScreen.Profile,
            onNavigate = onNavigate
        )
    }
}

@Composable
private fun ProfileOptionRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    showSwitch: Boolean = false,
    switchChecked: Boolean = false,
    onSwitchChange: (Boolean) -> Unit = {},
    textColor: Color = Color.Black,
    iconColor: Color = Color.Black,
    arrowColor: Color = Color.Black,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = iconColor
        )

        Spacer(modifier = Modifier.width(18.dp))

        Text(
            text = text,
            modifier = Modifier.weight(1f),
            color = textColor,
            fontWeight = FontWeight.Bold
        )

        if (showSwitch) {
            Switch(
                checked = switchChecked,
                onCheckedChange = onSwitchChange,
                colors = SwitchDefaults.colors(
                    checkedTrackColor = Color(0xFF7154B8)
                )
            )
        } else {
            IconButton(
                onClick = {},
                enabled = false) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Ir a $text",
                    tint = arrowColor,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }

    Divider(color = Color(0xFFE0E0E0))
}