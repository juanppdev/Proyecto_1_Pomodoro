package com.mundocode.pomodoro.ui.screens.loginScreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mundocode.pomodoro.R
import com.mundocode.pomodoro.core.navigation.Destinations
import kotlinx.serialization.ExperimentalSerializationApi
import com.kiwi.navigationcompose.typed.navigate as kiwiNavigation

@OptIn(ExperimentalSerializationApi::class)
@Composable
fun RegisterScreen(viewModel: LoginViewModel = hiltViewModel(), navController: NavController) {
    var email by rememberSaveable { mutableStateOf("") }
    var name by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    val loginSuccess by viewModel.loginSuccess.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    LaunchedEffect(loginSuccess) {
        if (loginSuccess) {
            navController.kiwiNavigation(Destinations.HomeScreen)
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEFEFEF)),
        contentAlignment = Alignment.Center,
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 16.dp, // Controla la elevación (sombra)
            ),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF333333), // Color de fondo de la tarjeta
            ),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(width = 1.dp, Color.Black),
        ) {
            Column(
                modifier = Modifier.padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "REGISTRO",
                    fontWeight = FontWeight.Bold,
                    fontSize = 29.sp,
                    color = Color.White,
                    modifier = Modifier.padding(top = 24.dp),
                )

                Spacer(modifier = Modifier.padding(16.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Cyan,
                        focusedTextColor = Color.White,
                        unfocusedContainerColor = Color.Gray,
                        unfocusedLabelColor = Color.LightGray,
                    ),
                    label = { Text("Nombre Completo") },
                )

                Spacer(modifier = Modifier.padding(8.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Cyan,
                        focusedTextColor = Color.White,
                        unfocusedContainerColor = Color.Gray,
                        unfocusedLabelColor = Color.LightGray,
                    ),
                    label = { Text("Correo Electronico") },
                )

                Spacer(modifier = Modifier.padding(8.dp))

                var hidden by rememberSaveable { mutableStateOf(true) }

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Cyan,
                        focusedTextColor = Color.White,
                        unfocusedContainerColor = Color.Gray,
                        unfocusedLabelColor = Color.LightGray,
                    ),
                    label = { Text("Contraseña") },

                    visualTransformation =
                    if (hidden) PasswordVisualTransformation() else VisualTransformation.None,

                    trailingIcon = {
                        IconButton(onClick = { hidden = !hidden }) {
                            val hiddenPassword = painterResource(
                                if (hidden) {
                                    R.drawable.visible
                                } else {
                                    R.drawable.no_visible
                                },
                            )
                            Icon(
                                painter = hiddenPassword,
                                contentDescription = "",
                                tint = Color.White,
                            )
                        }
                    },
                )

                Spacer(modifier = Modifier.padding(8.dp))

                if (errorMessage != null) {
                    Text(text = errorMessage!!, color = Color.Red)
                }

                Spacer(modifier = Modifier.padding(8.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Button(
                        onClick = {
                            navController.kiwiNavigation(Destinations.Login)
                        },
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
                        onClick = {
                            viewModel.registerWithEmail(name, email, password)
                        },
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
