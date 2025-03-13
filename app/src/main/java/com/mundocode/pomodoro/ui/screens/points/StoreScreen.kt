package com.mundocode.pomodoro.ui.screens.points

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mundocode.pomodoro.ui.components.CustomTopAppBar

@Composable
fun StoreScreen(
    navController: NavController,
    storeViewModel: StoreViewModel = hiltViewModel(),
    factoryProvider: PointsViewModelFactoryProvider = hiltViewModel(),
) {
    val userId = Firebase.auth.currentUser?.uid ?: ""
    val user = Firebase.auth.currentUser

    // Crear el ViewModel usando la factory del provider
    val pointsViewModel: PointsViewModel = viewModel(
        factory = PointsViewModel.provideFactory(
            assistedFactory = factoryProvider.pointsViewModelFactory,
            userId = userId,
        ),
    )

    val storeItems by storeViewModel.storeItems.collectAsState()
    val userPoints by pointsViewModel.userPoints.collectAsState()
    val purchasedItems by storeViewModel.purchasedDataEntity.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        storeViewModel.loadPurchasedData(userId)
    }

    Scaffold(
        topBar = {
            CustomTopAppBar(
                navController = navController,
                title = "Tienda",
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                image = user?.photoUrl.toString(),
                texto = "Puntos $userPoints",
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            LazyColumn {
                items(storeItems) { item ->
                    StoreCard(
                        name = item.name,
                        description = item.description,
                        price = item.price,
                        isPurchased = purchasedItems.any { it.itemName == item.name },
                        onPurchase = {
                            val success = storeViewModel.purchaseData(userId, item, userPoints)
                            Toast.makeText(
                                context,
                                if (success) "Compra exitosa" else "Puntos insuficientes",
                                Toast.LENGTH_SHORT,
                            ).show()
                        },
                    )
                }
            }
        }
    }
}

/** 📌 Componente reutilizable para Ítems y Temas */
@Composable
fun StoreCard(name: String, description: String, price: Int, isPurchased: Boolean, onPurchase: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = name,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = description,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = "Precio: $price puntos",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface,
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (!isPurchased) {
                Button(onClick = { onPurchase() }) {
                    Text("Comprar")
                }
            } else {
                Text(
                    text = "Comprado",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray,
                )
            }
        }
    }
}
