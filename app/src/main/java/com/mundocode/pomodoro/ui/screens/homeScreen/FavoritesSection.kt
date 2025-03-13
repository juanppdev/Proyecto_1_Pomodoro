package com.mundocode.pomodoro.ui.screens.homeScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FavoritesSection() {
    Column(modifier = Modifier.padding(8.dp)) {
        Text(
            text = "Tus favoritos",
            fontSize = 24.sp,
            modifier = Modifier.padding(8.dp),
            color = MaterialTheme.colorScheme.onSurface,
        )

        Spacer(modifier = Modifier.padding(4.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            FavouritesButtons(0xFFFF4E21)
            FavouritesButtons(0xFF6366F1)
            FavouritesButtons(0xFF900300)
        }
    }
}

@Composable
fun FavouritesButtons(color: Long) {
    Button(
        onClick = {},
        modifier = Modifier.size(120.dp),
        shape = RoundedCornerShape(24.dp),
        colors = ButtonDefaults.buttonColors(Color(color)),
    ) { }
}
