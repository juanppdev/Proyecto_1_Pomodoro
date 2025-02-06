package com.mundocode.pomodoro.ui.screens.loginScreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
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
fun LoginScreen(navController: NavController, viewModel: LoginViewModel = hiltViewModel()) {
    val loginSuccess by viewModel.loginSuccess.collectAsState()

    LaunchedEffect(loginSuccess) {
        if (loginSuccess) {
            navController.kiwiNavigation(Destinations.HomeScreen)
        }
    }

    Scaffold { padding ->
        LoginContent(
            modifier = Modifier.padding(padding),
            loginGoogleClicked = {
                viewModel.handleGoogleSignIn()
            },
        )
    }
}

@Composable
private fun LoginContent(modifier: Modifier = Modifier, loginGoogleClicked: () -> Unit) {
    Box(
        modifier = modifier
            .fillMaxSize(),
        // .background(Color(0xFFEFEFEF)),
        contentAlignment = Alignment.Center,
    ) {
        Card(
            modifier = modifier
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
                modifier = modifier.padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "POMODORAPP",
                    fontWeight = FontWeight.Bold,
                    fontSize = 29.sp,
                    color = Color.White,
                    modifier = modifier.padding(top = 24.dp),
                )

                DataLogin("Usuario / correo electrónico")

                PasswordLogin("Contraseña")
                Text(
                    text = "¿Has olvidado la contraseña?",
                    color = Color.White,
                    modifier = Modifier
                        .offset(x = (-40).dp)
                        .padding(top = 6.dp),
                )

                Spacer(modifier = modifier.padding(8.dp))

                Box(
                    modifier = modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    Button(
                        onClick = {},
                        modifier = modifier
                            .size(
                                width = 150.dp,
                                height = 48.dp,
                            ),
                        colors = ButtonDefaults.buttonColors(Color(0xFFB51C1C)),
                        shape = RoundedCornerShape(16.dp),
                    ) {
                        Text(text = "Iniciar sesión")
                    }
                }

                Row(
                    modifier = modifier
                        .padding(16.dp)
                        .clickable { loginGoogleClicked() },
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.google_logo),
                        contentDescription = "",
                        modifier = Modifier
                            .size(24.dp),
                    )
                    Text(
                        text = "Iniciar sesión con Google",
                        modifier = modifier.padding(8.dp),
                        fontSize = 16.sp,
                        color = Color.White,
                    )
                }

                Spacer(modifier = modifier.padding(16.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    contentAlignment = Alignment.BottomCenter,
                ) {
                    Text(
                        buildAnnotatedString {
                            append("¿No tienes cuenta aún? ¡Regístrate!")
                        },
                    )
                }
            }
        }
    }
}

@Composable
fun DataLogin(label: String) {
    var state by rememberSaveable { mutableStateOf("") }

    OutlinedTextField(
        value = state,
        onValueChange = { state = it },
        shape = RoundedCornerShape(16.dp),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Cyan,
            focusedTextColor = Color.White,
            unfocusedContainerColor = Color.Gray,
            unfocusedLabelColor = Color.LightGray,
        ),
        label = { Text(label) },
    )
}

@Composable
fun PasswordLogin(label: String) {
    var password by rememberSaveable { mutableStateOf("") }
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
        label = { Text(label) },

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
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(navController = NavController(LocalContext.current))
}
