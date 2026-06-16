package com.example.app.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCard
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app.navigation.MoneyFlowScreen

@Composable
fun BottomBar(
    selected: MoneyFlowScreen,
    onNavigate: (MoneyFlowScreen) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BottomItem(
            icon = Icons.Default.FavoriteBorder,
            label = "Home",
            selected = selected == MoneyFlowScreen.Home
        ) {
            onNavigate(MoneyFlowScreen.Home)
        }

        BottomItem(
            icon = Icons.Default.AddCard,
            label = "Gastos",
            selected = selected == MoneyFlowScreen.AddExpense
        ) {
            onNavigate(MoneyFlowScreen.AddExpense)
        }

        BottomItem(
            icon = Icons.Default.BookmarkBorder,
            label = "Ahorros",
            selected = selected == MoneyFlowScreen.Savings
        ) {
            onNavigate(MoneyFlowScreen.Savings)
        }

        BottomItem(
            icon = Icons.Default.AccountCircle,
            label = "Perfil",
            selected = selected == MoneyFlowScreen.Profile
        ) {
            onNavigate(MoneyFlowScreen.Profile)
        }
    }
}

@Composable
private fun BottomItem(
    icon: ImageVector,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val purple = Color(0xFF7154B8)
    val lightPurple = Color(0xFFE8DDFF)

    Column(
        modifier = Modifier.clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(if (selected) lightPurple else Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (selected) purple else Color.Black,
                modifier = Modifier.size(26.dp)
            )
        }

        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
            color = Color.Black
        )
    }
}