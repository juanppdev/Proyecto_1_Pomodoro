package com.mundocode.pomodoro.ui.screens.homeScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mundocode.pomodoro.core.navigation.Destinations
import com.mundocode.pomodoro.ui.components.CustomTopAppBar
import com.mundocode.pomodoro.ui.screens.points.PointsViewModel
import com.mundocode.pomodoro.ui.screens.points.PointsViewModelFactoryProvider
import com.mundocode.pomodoro.ui.screens.points.StoreViewModel
import kotlinx.serialization.ExperimentalSerializationApi
import com.kiwi.navigationcompose.typed.navigate as kiwiNavigation

@OptIn(ExperimentalSerializationApi::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    navController: NavController,
    factoryProvider: PointsViewModelFactoryProvider = hiltViewModel(),
    storeViewModel: StoreViewModel = hiltViewModel(),
) {
    val user = Firebase.auth.currentUser
    val userId = Firebase.auth.currentUser?.uid ?: ""

    // Crear el ViewModel usando la factory del provider
    val pointsViewModel: PointsViewModel = viewModel(
        factory = PointsViewModel.provideFactory(
            assistedFactory = factoryProvider.pointsViewModelFactory,
            userId = userId,
        ),
    )

    val sessionsData by viewModel.sessionsData.collectAsState()
    val xLabels by viewModel.xLabels.collectAsState()

    var selectedOption by remember { mutableStateOf("Weekly") }

    val userPoints by pointsViewModel.userPoints.collectAsState()
    val totalTime = viewModel.totalTimeData.collectAsState().value

    Scaffold(
        topBar = {
            CustomTopAppBar(
                navController = navController,
                title = "Pomodoro",
                image = user?.photoUrl.toString(),
                navigationIcon = {},
                texto = "Puntos: $userPoints",
                onNavPoints = {
                    navController.kiwiNavigation(Destinations.Store)
                },
            )
        },
        modifier = Modifier.fillMaxSize(),
    ) { padding ->

        val userId = Firebase.auth.currentUser?.uid ?: ""

        LaunchedEffect(Unit) {
            storeViewModel.loadPurchasedData(userId) // ✅ Cargar temas desbloqueados
        }

        LazyColumn {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = MaterialTheme.colorScheme.background)
                        .padding(padding),
                ) {
                    WelcomeSection(user)
//                    FavoritesSection()
                    OptionsSection(navController)
                    StatsSection(
                        selectedOption,
                        onOptionSelected = {
                            selectedOption = it
                            viewModel.loadSessions(it)
                        },
                        sessionsData,
                        xLabels,
                        totalTime,
                    )
                }
            }
        }
    }
}
