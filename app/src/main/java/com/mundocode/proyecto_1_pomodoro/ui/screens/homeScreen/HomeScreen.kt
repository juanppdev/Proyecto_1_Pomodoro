package com.mundocode.proyecto_1_pomodoro.ui.screens.homeScreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mundocode.proyecto_1_pomodoro.R

@Preview
@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFEFEFEF))
            .padding(16.dp),
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(text = "Bienvenido <nombre>",
                fontSize = 24.sp,
                modifier = Modifier.padding(8.dp)
            )
            Text(text = "¿Qué quieres hacer hoy?",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(8.dp)
            )
        }

        Column(modifier = Modifier.padding(8.dp)) {
            Text(text = "Tus favoritos",
                fontSize = 24.sp,
                modifier = Modifier.padding(8.dp))

            Spacer(modifier = Modifier.padding(4.dp))

            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly) {
                FavouritesButtons(0xFFFF4E21)
                FavouritesButtons(0xFF6366F1)
                FavouritesButtons(0xFF900300)
            }
        }

        Spacer(modifier = Modifier.padding(16.dp))

        Column(modifier = Modifier.padding(8.dp)) {
            OptionButtons(0xFFB51C1C,
                "Empezar\nPomodoro",
                R.drawable.timer_icon,
                "botón de Empezar Pomodoro")


            OptionButtons(0xFF06B6D4,
                "Ver\nHábitos",
                R.drawable.habit_icon,
                "botón Ver Hábitos")

            OptionButtons(0xFF6366F1,
                "Ver\nTareas",
                R.drawable.checklist_icon,
                "botón Ver Tareas")
        }

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Estadísticas",
                fontSize = 24.sp,
                modifier = Modifier.padding(8.dp))
            Button(
                onClick = {},
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.padding(8.dp),
                border = BorderStroke(2.dp, color = Color.Black),
                colors = ButtonDefaults.buttonColors(Color.Transparent)
            ) {
                Row(modifier = Modifier.size(
                            width = 64.dp,
                            height = 32.dp
                        ),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Weekly",
                        color = Color.Black
                    )
                    Icon(
                        imageVector = Icons.Rounded.KeyboardArrowDown,
                        contentDescription = "",
                        tint = Color.Black
                    )
                }

            }
        }
    }
}

@Composable
fun FavouritesButtons(color: Long) {
    Button(onClick = {},
        modifier = Modifier.size(120.dp),
        shape = RoundedCornerShape(24.dp),
        colors = ButtonDefaults.buttonColors(Color(color))
    ) { }
}

@Composable
fun OptionButtons(color: Long, textButton: String,
                  icon: Int, descriptionIcon: String) {
    Button(onClick = {},
        shape = RoundedCornerShape(24.dp),
        colors = ButtonDefaults.buttonColors(Color(color)),
        modifier = Modifier.padding(8.dp)
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .size(80.dp)
            .clip(RoundedCornerShape(24.dp))
            .padding(
                start = 72.dp,
                end = 72.dp
            ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = textButton,
                fontSize = 24.sp,
                fontWeight = FontWeight.Black,
                color = Color.White,
                lineHeight = 24.sp
            )
            Icon(
                painterResource(id = icon),
                contentDescription = descriptionIcon,
                modifier = Modifier
                    .size(58.dp)
            )
        }
    }
}