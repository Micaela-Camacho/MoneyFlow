package com.example.moneyflow.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SplashScreen(
    onStartClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp)
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("📈", fontSize = 90.sp)

            Text(
                text = "MoneyFlow",
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF7154B8)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Organizá tus finanzas,\nalcanzá tus metas.",
                fontSize = 16.sp
            )
        }

        Button(
            onClick = onStartClick,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(bottom = 48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF7154B8)
            )
        ) {
            Text("Comenzar")
        }
    }
}