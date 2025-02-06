package com.mundocode.pomodoro.ui.screens.loginScreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mundocode.pomodoro.R

@Preview
@Composable
fun RegisterScreen() {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEFEFEF)),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 16.dp // Controla la elevación (sombra)
            ),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF333333) // Color de fondo de la tarjeta
            ),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(width = 1.dp, Color.Black)
        ) {
            Column(
                modifier = Modifier.padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "REGISTRO",
                    fontWeight = FontWeight.Bold,
                    fontSize = 29.sp,
                    color = Color.White,
                    modifier = Modifier.padding(top = 24.dp)
                )

                Spacer(modifier = Modifier.padding(16.dp))

                DataRegister("Nombre:")

                Spacer(modifier = Modifier.padding(8.dp))

                DataRegister("Usuario / correo electrónico")

                Spacer(modifier = Modifier.padding(8.dp))

                PasswordRegister("Contraseña")

                Spacer(modifier = Modifier.padding(8.dp))

                Row (
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {},
                        modifier = Modifier
                            .padding(8.dp)
                            .size(
                                width = 120.dp,
                                height = 48.dp,
                            ),
                        colors = ButtonDefaults.buttonColors(Color.Gray),
                        shape = RoundedCornerShape(16.dp),
                    ) {
                        Text(text = "Cancelar")
                    }

                    Button(
                        onClick = {},
                        modifier = Modifier
                            .padding(8.dp)
                            .size(
                                width = 120.dp,
                                height = 48.dp,
                            ),
                        colors = ButtonDefaults.buttonColors(Color(0xFFB51C1C)),
                        shape = RoundedCornerShape(16.dp),
                    ) {
                        Text(text = "Registro")
                    }
                }
                Spacer(modifier = Modifier.padding(bottom = 16.dp))
            }
        }
    }
}

@Composable
fun DataRegister(label: String) {
    var state by rememberSaveable { mutableStateOf("") }

    OutlinedTextField(
        value = state,
        onValueChange = {state = it},
        shape = RoundedCornerShape(16.dp),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Cyan,
            focusedTextColor = Color.White,
            unfocusedContainerColor = Color.Gray,
            unfocusedLabelColor = Color.LightGray
        ),
        label = { Text(label) }
    )
}

@Composable
fun PasswordRegister(label: String) {
    var password by rememberSaveable { mutableStateOf("") }
    var hidden by rememberSaveable { mutableStateOf(true) }

    OutlinedTextField(
        value = password,
        onValueChange = {password = it},
        shape = RoundedCornerShape(16.dp),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Cyan,
            focusedTextColor = Color.White,
            unfocusedContainerColor = Color.Gray,
            unfocusedLabelColor = Color.LightGray
        ),
        label = { Text(label) },

        visualTransformation =
        if (hidden) PasswordVisualTransformation() else VisualTransformation.None,

        trailingIcon = {
            IconButton(onClick = {hidden = !hidden}) {
                val hiddenPassword = painterResource(
                    if (hidden) R.drawable.visible
                    else R.drawable.no_visible
                )
                Icon(
                    painter = hiddenPassword,
                    contentDescription = "",
                    tint = Color.White)
            }
        }
    )
}
