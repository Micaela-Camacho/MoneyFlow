package com.example.app.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app.UsuarioViewModel
import com.example.app.components.BottomBar
import com.example.app.navigation.MoneyFlowScreen

@Composable
fun ProfileScreen(
    viewModel: UsuarioViewModel,
    darkModeEnabled: Boolean,
    onDarkModeChange: (Boolean) -> Unit,
    onNavigate: (MoneyFlowScreen) -> Unit
)
{
    // 1. Escucha al usuario logueado en tiempo real
    val usuarioActual by viewModel.usuarioActual.collectAsState()

    // 2. Variables dinámicas (si no hay nadie logueado por error, muestra un texto por defecto)
    val nombreMostrar = usuarioActual?.nombre ?: "Nombre Usuario"
    val emailMostrar = usuarioActual?.email ?: "usuario@usuario.com"
    val sueldoMostrar = usuarioActual?.sueldoMensual ?: 0.0


    val purple = Color(0xFF7154B8)
    val lightPurple = Color(0xFFE8DDFF)
    val veryLightPurple = Color(0xFFFFF7FF)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 28.dp, vertical = 48.dp)
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
                    modifier = Modifier.size(70.dp)
                )
            }

            Spacer(modifier = Modifier.width(18.dp))

            Column {
                // 3. DATOS REALES DE LA BASE DE DATOS
                Text(
                    text = nombreMostrar,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = emailMostrar,
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(54.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = veryLightPurple
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp
            )
        ) {
            Column(
                modifier = Modifier.padding(18.dp)
            ) {
                Text(
                    text = "Información financiera",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 3.dp
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

                            // 4. Formateamos el sueldo real del usuario con formato regional de Argentina
                            Text(
                                text = "$${String.format(java.util.Locale("es", "AR"), "%,.2f", sueldoMostrar)}",
                                fontSize = 26.sp,
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

        Spacer(modifier = Modifier.height(48.dp))

        ProfileOptionRow(
            icon = Icons.Default.Category,
            text = "Categorías",
            showArrow = true
        )

        ProfileOptionRow(
            icon = Icons.Default.Flag,
            text = "Metas de ahorro",
            showArrow = true,
            onClick = {
                onNavigate(MoneyFlowScreen.Savings)
            }
        )

        ProfileOptionRow(
            icon = Icons.Default.DarkMode,
            text = "Dark mode",
            showSwitch = true,
            switchChecked = darkModeEnabled,
            onSwitchChange = onDarkModeChange
        )

        ProfileOptionRow(
            icon = Icons.Default.Settings,
            text = "Configuración",
            showArrow = true
        )

        ProfileOptionRow(
            icon = Icons.Default.Logout,
            text = "Cerrar sesión",
            showArrow = true,
            textColor = Color.Red,
            iconColor = Color.Red,
            arrowColor = Color.Red,
            onClick = {
                // 5. Lógica para el botón de Cerrar Sesión
                viewModel.cerrarSesion() // Borra el usuario de la memoria
                onNavigate(MoneyFlowScreen.Login) // Lo manda al Login de nuevo
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
    showArrow: Boolean = false,
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
            .height(48.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = iconColor,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(22.dp))

        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = textColor,
            modifier = Modifier.weight(1f)
        )

        if (showArrow) {
            IconButton(onClick = onClick) {
                Icon(
                    imageVector = Icons.Default.ArrowForwardIos,
                    contentDescription = "Ir a $text",
                    tint = arrowColor,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        if (showSwitch) {
            Switch(
                checked = switchChecked,
                onCheckedChange = onSwitchChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = Color(0xFF7154B8)
                )
            )
        }
    }
}