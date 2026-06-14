package com.example.moneyflow.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.moneyflow.UsuarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    viewModel: UsuarioViewModel, // 1. Recibe el motor de usuarios desde el MainActivity
    onBackClick: () -> Unit,
    onCreateAccountClick: () -> Unit,
    onLoginClick: () -> Unit
) {

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("")}
    // 2. Variable para capturar el sueldo mensual base obligatorio
    var salary by remember { mutableStateOf("")}

    // Estado local para mostrar alertas si algo sale mal (ej: contraseñas distintas)
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {

        TextButton(onClick = onBackClick) {
            Text("← Volver")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Crear cuenta",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Si hay un error, muestra un cartelito rojo antes de los inputs
        errorMessage?.let { error ->
            Text(text = error, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(bottom = 8.dp))
        }

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 3. CAMPO DE SUELDO MENSUAL
        OutlinedTextField(
            value = salary,
            onValueChange = { salary = it },
            label = { Text("Sueldo Mensual Base") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirmar contraseña") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 4. CONEXIÓN DEL BOTÓN CON EL BACKEND REAL
        Button(
            onClick = {
                val sueldoDouble = salary.toDoubleOrNull()

                // Validaciones básicas de seguridad antes de pegarle a Room
                when {
                    name.isBlank() || email.isBlank() || password.isBlank() || salary.isBlank() -> {
                        errorMessage = "Por favor, completa todos los campos."
                    }
                    password != confirmPassword -> {
                        errorMessage = "Las contraseñas no coinciden."
                    }
                    sueldoDouble == null || sueldoDouble < 0 -> {
                        errorMessage = "Ingresa un sueldo mensual válido."
                    }
                    else -> {
                        errorMessage = null // Limpiamos errores previos

                        // Le pedimos al ViewModel que inserte la fila en SQLite de forma asíncrona
                        viewModel.registrarNuevoUsuario(
                            nombre = name,
                            email = email,
                            contrasenia = password,
                            sueldo = sueldoDouble,
                            onResult = { exito ->
                                if (exito) {
                                    onCreateAccountClick() // Registro exitoso -> Avanza a la Home
                                } else {
                                    errorMessage = "Error al registrar. El email podría estar en uso."
                                }
                            }
                        )
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Crear cuenta")
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onLoginClick) {
            Text("Ya tengo cuenta")
        }
    }
}