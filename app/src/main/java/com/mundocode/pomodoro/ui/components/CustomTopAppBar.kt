package com.mundocode.pomodoro.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.IconButton
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.mundocode.pomodoro.core.navigation.Destinations
import kotlinx.serialization.ExperimentalSerializationApi
import com.kiwi.navigationcompose.typed.navigate as kiwiNavigation

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSerializationApi::class)
@Composable
fun CustomTopAppBar(
    navController: NavController,
    title: String,
    image: String,
    navigationIcon: @Composable () -> Unit = {},
    texto: String,
    onNavPoints: () -> Unit = {},
) {
    var isMenuExpanded by remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.inverseSurface,
            )
        },
        navigationIcon = navigationIcon,
        actions = {
            Text(
                text = texto,
                modifier = Modifier.padding(horizontal = 30.dp).clickable(
                    onClick = {
                        onNavPoints()
                    },
                ),
                color = MaterialTheme.colorScheme.inverseSurface,
            )

            IconButton(onClick = { isMenuExpanded = true }) {
                AsyncImage(
                    model = image,
                    contentDescription = "Avatar de usuario",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                )
            }
            DropdownMenu(
                expanded = isMenuExpanded,
                onDismissRequest = { isMenuExpanded = false },
                offset = DpOffset(250.dp, 0.dp), // 🔹 Ajuste para mover el menú a la derecha si es necesario
                modifier = Modifier.background(MaterialTheme.colorScheme.surface),
            ) {
                DropdownMenuItem(
                    onClick = {
                        isMenuExpanded = false
                        navController.kiwiNavigation(Destinations.SettingsScreen)
                    },
                    text = {
                        Text("Configuración")
                    },
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
    )
}

@Preview
@Composable
fun PreviewCustomTopAppBar() {
    CustomTopAppBar(
        navController = NavController(LocalContext.current),
        title = "Custom Top App Bar",
        image = "https://example.com/avatar.jpg",
        texto = "Puntos: 50",
    )
}
