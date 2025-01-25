package com.mundocode.pomodoro.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopAppBar(
    navController: NavController,
    title: String,
    navigationIcon: @Composable () -> Unit = {},
) {
    var isMenuExpanded by remember { mutableStateOf(false) }

    TopAppBar(
        title = { Text(text = title) },
        navigationIcon = navigationIcon,
        actions = {
            IconButton(onClick = { isMenuExpanded = true }) {
                Icon(
                    imageVector = Icons.Outlined.AccountCircle,
                    contentDescription = "Open Menu",
                    modifier = Modifier.size(60.dp)
                )
            }
            DropdownMenu(
                expanded = isMenuExpanded,
                onDismissRequest = { isMenuExpanded = false },
            ) {
                DropdownMenuItem(
                    onClick = {
                        isMenuExpanded = false
                        navController.navigate("screen1")
                    },
                    text = {
                        Text("Screen 1")
                    },
                )
                DropdownMenuItem(
                    onClick = {
                        isMenuExpanded = false
                        navController.navigate("screen2")
                    },
                    text = {
                        Text("Screen 2")
                    },
                )
                DropdownMenuItem(
                    onClick = {
                        isMenuExpanded = false
                        navController.navigate("screen3")
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
        )
    )
}

@Preview
@Composable
fun PreviewCustomTopAppBar() {
    CustomTopAppBar(
        navController = NavController(LocalContext.current),
        title = "Custom Top App Bar",
    )
}
