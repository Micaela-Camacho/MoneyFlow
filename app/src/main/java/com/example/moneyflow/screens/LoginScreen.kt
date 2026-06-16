package com.example.moneyflow.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.example.moneyflow.UsuarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: UsuarioViewModel, // 1.Recibe el motor de Usuarios desde el MainActivity
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    onForgotPasswordClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    // Estado local para avisarle al usuario si escribió mal los datos
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var loginError by remember { mutableStateOf<String?>(null) }

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

        Text("Email")
        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                emailError = null
                loginError = null
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
            shape = RoundedCornerShape(14.dp),
            isError = emailError != null
        )

        emailError?.let { error ->
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 4.dp),
                fontSize = 12.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Contraseña")
        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                passwordError = null
                loginError = null
            },
            placeholder = { Text("********") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Contraseña"
                )
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            visualTransformation =
            if (passwordVisible)
                VisualTransformation.None
            else
                PasswordVisualTransformation(),
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
            isError = passwordError != null
        )

        passwordError?.let { error ->
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 4.dp),
                fontSize = 12.sp
            )
        }

        loginError?.let { error ->
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 6.dp),
                fontSize = 12.sp
            )
        }

        TextButton(
            onClick = onForgotPasswordClick,
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
                emailError = null
                passwordError = null
                loginError = null

                if (email.isBlank()) {
                    emailError = "Ingresá tu email."
                }

                if (password.isBlank()) {
                    passwordError = "Ingresá tu contraseña."
                }

                if (email.isNotBlank() && password.isNotBlank()) {
                    // Busca en segundo plano si coinciden mail y contraseña
                    viewModel.iniciarSesion(
                        email = email,
                        contrasenia = password,
                        onResult = { loginExitoso ->
                            if (loginExitoso) {
                                onLoginClick() // Datos correctos, Pasa a la Home
                            } else {
                                loginError = "Email o contraseña incorrectos."
                            }
                        }
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(28.dp),
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
                buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Color.Black)) {
                        append("¿No tenés cuenta? ")
                    }
                    withStyle(
                        style = SpanStyle(
                            color = Color(0xFF7154B8),
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        append("Registrate")
                    }
                }
            )
        }
    }
}