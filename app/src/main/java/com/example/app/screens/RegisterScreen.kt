package com.example.app.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SentimentSatisfied
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app.UsuarioViewModel

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
    var confirmPassword by remember { mutableStateOf("") }

    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    // 2. Variable para capturar el sueldo mensual base obligatorio
    var salary by remember { mutableStateOf("") }

    // Estado local para mostrar alertas si algo sale mal (ej: contraseñas distintas)
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val purple = Color(0xFF7154B8)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 28.dp, vertical = 48.dp)
    ) {

        TextButton(onClick = onBackClick) {
            Text(
                text = "← Volver",
                color = Color(0xFF007A9A)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Crear cuenta",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Completá tus datos para empezar",
            fontSize = 13.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(22.dp))

        // Si hay un error, muestra un cartelito rojo antes de los inputs
        errorMessage?.let { error ->
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(bottom = 8.dp),
                fontSize = 13.sp
            )
        }

        OutlinedTextField(
            value = name,
            onValueChange = {
                name = it
                errorMessage = null
            },
            placeholder = { Text("Tu nombre") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Nombre"
                )
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(14.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                errorMessage = null
            },
            placeholder = { Text("correo@ejemplo.com") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = "Email"
                )
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(14.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                errorMessage = null
            },
            placeholder = { Text("********") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Contraseña"
                )
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        passwordVisible = !passwordVisible
                    }
                ) {
                    Icon(
                        imageVector =
                        if (passwordVisible)
                            Icons.Default.VisibilityOff
                        else
                            Icons.Default.Visibility,
                        contentDescription = "Mostrar u ocultar contraseña"
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            visualTransformation =
            if (passwordVisible)
                VisualTransformation.None
            else
                PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = {
                confirmPassword = it
                errorMessage = null
            },
            placeholder = { Text("********") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Confirmar contraseña"
                )
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        confirmPasswordVisible = !confirmPasswordVisible
                    }
                ) {
                    Icon(
                        imageVector =
                        if (confirmPasswordVisible)
                            Icons.Default.VisibilityOff
                        else
                            Icons.Default.Visibility,
                        contentDescription = "Mostrar u ocultar confirmación de contraseña"
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            visualTransformation =
            if (confirmPasswordVisible)
                VisualTransformation.None
            else
                PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 3. CAMPO DE SUELDO MENSUAL
        OutlinedTextField(
            value = salary,
            onValueChange = {
                salary = it
                errorMessage = null
            },
            placeholder = { Text("Ej: 500000") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.SentimentSatisfied,
                    contentDescription = "Sueldo mensual"
                )
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(14.dp)
        )

        Spacer(modifier = Modifier.height(28.dp))

        // 4. CONEXIÓN DEL BOTÓN CON EL BACKEND REAL
        Button(
            onClick = {
                val sueldoDouble = salary.toDoubleOrNull()

                // Validaciones básicas de seguridad antes de pegarle a Room
                when {
                    name.isBlank() || email.isBlank() || password.isBlank() || confirmPassword.isBlank() || salary.isBlank() -> {
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
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = purple
            )
        ) {
            Text(
                text = "Crear cuenta",
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        TextButton(
            onClick = onLoginClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Color.Black)) {
                        append("¿Ya tenés cuenta? ")
                    }
                    withStyle(
                        style = SpanStyle(
                            color = purple,
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        append("Inicia sesión")
                    }
                }
            )
        }
    }
}