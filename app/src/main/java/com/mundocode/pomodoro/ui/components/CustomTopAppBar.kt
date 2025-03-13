package com.mundocode.pomodoro.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kiwi.navigationcompose.typed.createRoutePattern
import com.mundocode.pomodoro.core.navigation.Destinations
import com.mundocode.pomodoro.ui.screens.points.StoreViewModel
import com.mundocode.pomodoro.ui.theme.ThemeViewModel
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
    storeViewModel: StoreViewModel = hiltViewModel(),
    themeViewModel: ThemeViewModel = hiltViewModel(),
) {
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }
    val unlockedThemes by storeViewModel.unlockedThemes.collectAsState()
    val currentTheme by themeViewModel.selectedTheme.collectAsState()
    val userId = Firebase.auth.currentUser?.uid ?: ""

    LaunchedEffect(Unit) {
        storeViewModel.loadPurchasedData(userId) // ✅ Recargar los temas desbloqueados
    }

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
                modifier = Modifier
                    .padding(horizontal = 30.dp)
                    .clickable(
                        onClick = {
                            onNavPoints()
                        },
                    ),
                color = MaterialTheme.colorScheme.inverseSurface,
            )

            IconButton(onClick = { showBottomSheet = true }) {
                AsyncImage(
                    model = image,
                    contentDescription = "Avatar de usuario",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
    )

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = false
            },
            sheetState = sheetState,
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
            containerColor = MaterialTheme.colorScheme.surface,
            dragHandle = {
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .width(50.dp)
                        .height(6.dp)
                        .clip(RoundedCornerShape(50))
                        .background(MaterialTheme.colorScheme.onSurface),
                )
            },
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .height(750.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    "Configuración",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.height(16.dp))
                // Aqui va lo que estaba en la configuracion
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
                        selectedTheme = currentTheme,
                        onThemeSelected = { themeViewModel.changeTheme(it) },
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "Cerrar Sesión",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        Firebase.auth.signOut()
                        navController.kiwiNavigation(Destinations.Login) {
                            popUpTo(createRoutePattern<Destinations.Home>()) { inclusive = true } // ✅ Usamos el patrón de ruta
                        }
                    },
                )
            }
        }
    }
}
