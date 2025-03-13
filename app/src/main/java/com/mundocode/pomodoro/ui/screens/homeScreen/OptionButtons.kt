package com.mundocode.pomodoro.ui.screens.homeScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun OptionButtons(color: Color, textButton: String, icon: Int, descriptionIcon: String, onClick: () -> Unit = {}) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(24.dp),
        colors = ButtonDefaults.buttonColors(color),
        modifier = Modifier.padding(8.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .size(80.dp)
                .clip(RoundedCornerShape(24.dp))
                .padding(
                    start = 72.dp,
                    end = 72.dp,
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = textButton,
                fontSize = 24.sp,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.onPrimary,
                lineHeight = 24.sp,
            )
            Icon(
                painterResource(id = icon),
                contentDescription = descriptionIcon,
                modifier = Modifier
                    .size(58.dp),
            )
        }
    }
}
