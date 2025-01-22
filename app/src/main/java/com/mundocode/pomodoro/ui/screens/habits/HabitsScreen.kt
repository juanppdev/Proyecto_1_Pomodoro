package com.mundocode.pomodoro.ui.screens.habits

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mundocode.pomodoro.R
import com.mundocode.pomodoro.viewModels.habitsViewModel.HabitsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitsScreen(viewModel: HabitsViewModel = hiltViewModel(), navController: NavController) {
    MaterialTheme {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(text = stringResource(id = R.string.app_name))
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                tint = MaterialTheme.colorScheme.onSurface,
                                contentDescription = "Localized description",
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.primary,
                    ),
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { viewModel.togglePopup() },
                    containerColor = Color(0xFF06B6D4),
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.add_icon),
                        contentDescription = "Agregar",
                    )
                }
            },
        ) { innerPadding ->
            // Contenido principal de la pantalla
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start,
            ) {
                // TextView
                Text(
                    text = "Mis Hábitos",
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                )

                // Espacio entre el buscador y la lista
                Spacer(modifier = Modifier.height(16.dp))

                // Buscador (TextField)
                var searchQuery by remember { mutableStateOf("") }
                TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Buscar") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Search,
                    ),
                    modifier = Modifier.fillMaxWidth(),
                )

                // Espacio entre el buscador y la lista
                Spacer(modifier = Modifier.height(16.dp))

                // RecyclerView (LazyColumn)
                val items = listOf("Elemento 1", "Elemento 2", "Elemento 3", "Elemento 4", "Elemento 5")
                LazyColumn {
                    items(items) { item ->
                        MyCard(item)
                    }
                }
            }
        }
        val showPopup by viewModel.showPopup.collectAsState()
        if (showPopup) {
            TextInputPopup(viewModel = viewModel, onDismiss = { viewModel.togglePopup() })
        }
    }
}

@Composable
fun MyCard(item: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start,
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            // Título
            Text(
                text = item, // Muestra el elemento de la lista
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Descripción
            Text(
                text = "Esta es una descripción de ejemplo para la tarjeta. Aquí puedes agregar más información.",
                fontSize = 16.sp,
                color = Color.Gray,
            )
        }
    }
}

@Composable
fun TextInputPopup(viewModel: HabitsViewModel, onDismiss: () -> Unit) {
    val tittle by viewModel.tittle.collectAsState()
    val description by viewModel.description.collectAsState()

    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(shape = RoundedCornerShape(8.dp), shadowElevation = 8.dp) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Nuevo hábito")

                TextField(
                    value = tittle,
                    onValueChange = { viewModel.updateTittle(it) },
                    label = { Text("Titulo") },
                )

                Spacer(modifier = Modifier.height(4.dp))

                TextField(
                    value = description,
                    onValueChange = { viewModel.updateDescription(it) },
                    label = { Text("Descripción") },
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Column(
                        modifier = Modifier.weight(1f).padding(8.dp), // Usar weight para que ocupe el mismo espacio
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Button(
                            onClick = {
                                viewModel.onAddClick()
                                onDismiss()
                            },
                            modifier = Modifier.padding(end = 8.dp), // Espaciado entre botones
                        ) {
                            Text("Guardar")
                        }

                    }
                    Column(
                        modifier = Modifier.weight(1f).padding(8.dp), // Usar weight para que ocupe el mismo espacio
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                    Button(onClick = { onDismiss() }) {
                        Text("Cancelar")
                    }
                }}
            }
        }
    }
}

