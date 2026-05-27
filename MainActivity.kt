package com.example.moneyflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.moneyflow.ui.theme.AddGastoScreen
import com.example.moneyflow.ui.theme.DashboardScreen
import com.example.moneyflow.ui.theme.LoginScreen
import com.example.moneyflow.ui.theme.MoneyFlowTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MoneyFlowTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // ESTADO DE NAVEGACIÓN: Guarda un texto que dice qué pantalla mostrar.
                    // Empieza en "login" (Pantalla de Iniciar Sesión).
                    var pantallaActual by remember { mutableStateOf("login") }

                    // ENRUTADOR: Evalua el valor de la variable para decidir qué dibujar
                    when (pantallaActual) {

                        // Si dice "login", dibuja la LoginScreen. Cuando se toca el botón, pasa a "dashboard"
                        "login" -> LoginScreen(onNavigateToDashboard = {
                            pantallaActual = "dashboard"
                        })

                        // Si dice "dashboard", dibuja el panel. Al tocar "+ Agregar", pasa a "add_gasto"
                        "dashboard" -> DashboardScreen(onNavigateToAddGasto = {
                            pantallaActual = "add_gasto"
                        })

                        // Si dice "add_gasto", dibuja el formulario. Al tocar "Guardar", regresa a "dashboard"
                        "add_gasto" -> AddGastoScreen(onNavigateBack = {
                            pantallaActual = "dashboard"
                        })
                    }
                }
            }
        }
    }
}


