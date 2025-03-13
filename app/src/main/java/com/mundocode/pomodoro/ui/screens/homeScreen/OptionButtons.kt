package com.mundocode.pomodoro.ui.screens.homeScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun OptionButtons(color: Color, textButton: String, icon: Int, descriptionIcon: String, onClick: () -> Unit = {}) {
    val configuration = LocalConfiguration.current

    // ✅ Ajustar tamaños dinámicamente en base al ancho de la pantalla
    val textSize = remember(configuration) {
        (24f * (configuration.screenWidthDp / 380f)).coerceAtLeast(16f).sp
    }

    val iconSize = remember(configuration) {
        (58f * (configuration.screenWidthDp / 380f)).coerceAtLeast(36f).dp
    }

    Button(
        onClick = onClick,
        shape = RoundedCornerShape(24.dp),
        colors = ButtonDefaults.buttonColors(color),
        modifier = Modifier
            .padding(8.dp)
            .height(80.dp), // ✅ Ajuste para evitar desbordamientos
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = textButton,
                fontSize = textSize,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.onPrimary,
                lineHeight = textSize,
                modifier = Modifier.weight(1f),
            )
            Icon(
                painter = painterResource(id = icon),
                contentDescription = descriptionIcon,
                modifier = Modifier.size(iconSize),
            )
        }
    }
}
