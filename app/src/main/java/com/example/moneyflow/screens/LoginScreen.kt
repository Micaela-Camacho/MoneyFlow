package com.example.moneyflow.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moneyflow.UsuarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: UsuarioViewModel, // 1.Recibe el motor de Usuarios desde el MainActivity
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Estado local para avisarle al usuario si escribió mal los datos
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 28.dp, vertical = 48.dp)
    ) {
        Text(
            text = "Iniciar sesión",
            fontSize = 26.sp
        )

        Text(
            text = "Ingresá para continuar",
            fontSize = 13.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Si los datos no coinciden en Room, muestra el cartelito de error
        errorMessage?.let { error ->
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(bottom = 8.dp),
                fontSize = 14.sp
            )
        }

        Text("Email")
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = { Text("correo@ejemplo.com") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Contraseña")
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = { Text("********") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation()
        )

        TextButton(
            onClick = {},
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "¿Olvidaste tu contraseña?",
                color = Color(0xFF7154B8)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // 2. CONECTA EL BOTÓN CON ROOM
        Button(
            onClick = {
                if (email.isBlank() || password.isBlank()) {
                    errorMessage = "Por favor, completa todos los campos."
                } else {
                    errorMessage = null // Limpia errores viejos

                    // Busca en segundo plano si coinciden mail y contraseña
                    viewModel.iniciarSesion(
                        email = email,
                        contrasenia = password,
                        onResult = { loginExitoso ->
                            if (loginExitoso) {
                                onLoginClick() // Datos correctos, Pasa a la Home
                            } else {
                                errorMessage = "Email o contraseña incorrectos."
                            }
                        }
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF7154B8)
            )
        ) {
            Text("Ingresar")
        }

        TextButton(
            onClick = onRegisterClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "¿No tenés cuenta? Registrate",
                color = Color(0xFF7154B8)
            )
        }
    }
}