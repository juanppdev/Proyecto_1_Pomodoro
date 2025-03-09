package com.mundocode.pomodoro.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kiwi.navigationcompose.typed.navigate as kiwiNavigation
import com.mundocode.pomodoro.core.navigation.Destinations
import com.mundocode.pomodoro.ui.components.CustomTopAppBar
import com.mundocode.pomodoro.ui.screens.SharedPointsViewModel
import com.mundocode.pomodoro.ui.screens.points.StoreViewModel
import com.mundocode.pomodoro.ui.theme.ThemeViewModel
import kotlinx.serialization.ExperimentalSerializationApi

@OptIn(ExperimentalSerializationApi::class)
@Composable
fun SettingsScreen(
    navController: NavHostController,
    sharedPointsViewModel: SharedPointsViewModel = hiltViewModel(),
    storeViewModel: StoreViewModel = hiltViewModel(),
    themeViewModel: ThemeViewModel = hiltViewModel(),
) {
    val user = Firebase.auth.currentUser
    val userPoints by sharedPointsViewModel.userPoints.collectAsState()
    val unlockedThemes by storeViewModel.unlockedThemes.collectAsState() // ✅ Asegurar que los temas desbloqueados se actualicen
    val currentTheme by themeViewModel.currentTheme.collectAsState()
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(currentTheme) }

    LaunchedEffect(Unit) {
        storeViewModel.loadPurchasedThemes() // ✅ Recargar los temas desbloqueados
    }

    Scaffold(
        topBar = {
            CustomTopAppBar(
                navController = navController,
                title = "Configuración",
                image = user?.photoUrl.toString(),
                navigationIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier.clickable {
                            navController.popBackStack()
                        },
                    )
                },
                texto = "Puntos: $userPoints",
                onNavPoints = {
                    navController.kiwiNavigation(Destinations.StoreScreen)
                },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("Tema")
                ThemeSelector(
                    unlockedThemes = unlockedThemes,
                    currentTheme = currentTheme,
                    onThemeSelected = { theme ->
                        themeViewModel.changeTheme(theme)
                    },
                )
            }
        }
    }
}

@Composable
fun ThemeSelector(unlockedThemes: Set<String>, currentTheme: String, onThemeSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(currentTheme) }

    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
        // 🔹 Asegurar alineación correcta
        OutlinedButton(
            modifier = Modifier.padding(10.dp),
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.surface),
            onClick = { expanded = true },
        ) {
            Row {
                Text(selectedOption, color = MaterialTheme.colorScheme.onSurface)
                Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Dropdown")
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            offset = DpOffset(250.dp, 0.dp), // 🔹 Ajuste para mover el menú a la derecha si es necesario
            modifier = Modifier.background(MaterialTheme.colorScheme.surface),
        ) {
            unlockedThemes.forEach { theme ->
                DropdownMenuItem(
                    text = { Text(theme) },
                    onClick = {
                        selectedOption = theme
                        onThemeSelected(theme)
                        expanded = false
                    },
                )
            }
        }
    }
}
