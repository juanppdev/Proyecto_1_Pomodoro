package com.mundocode.pomodoro.ui.screens.habits

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import com.mundocode.pomodoro.R
import com.mundocode.pomodoro.ui.components.CustomTopAppBar
import com.mundocode.pomodoro.ui.screens.habits.model.HabitsModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitsScreen(viewModel: HabitsViewModel = hiltViewModel(), navController: NavController) {
    val showDialog: Boolean by viewModel.showDialog.observeAsState(false)
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    val uiState by produceState<HabitsUIState>(
        initialValue = HabitsUIState.Loading,
        key1 = lifecycle,
        key2 = viewModel,
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            viewModel.uiState.collect { value = it }
        }
    }

    MaterialTheme {
        Scaffold(
            topBar = {
                CustomTopAppBar(
                    navController = navController,
                    title = stringResource(R.string.app_name),
                    navigationIcon = {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                tint = MaterialTheme.colorScheme.onSurface,
                                contentDescription = "Localized description",
                            )
                        }
                    },
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { viewModel.onShowDialogSelected() },
                    containerColor = Color(0xFF06B6D4),
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.add_icon),
                        contentDescription = "Agregar",
                    )
                }
            },
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start,
            ) {
                Text(
                    text = "Mis Hábitos",
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Buscador de hábitos
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

                Spacer(modifier = Modifier.height(16.dp))

                when (uiState) {
                    is HabitsUIState.Error -> {
                        val error = uiState as HabitsUIState.Error
                        Text(text = error.throwable.message ?: "Error desconocido")
                    }

                    HabitsUIState.Loading -> {
                        CircularProgressIndicator()
                    }

                    is HabitsUIState.Success -> {
                        TextInputPopup(
                            showDialog,
                            onDismiss = { viewModel.onDialogClose() },
                            onTaskAdded = { tittle, description -> viewModel.onTaskCreated(tittle, description) },
                        )

                        TasksList(
                            (uiState as HabitsUIState.Success).tasks,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TasksList(tasks: List<HabitsModel>) {
    LazyColumn {
        items(tasks.size) { task ->
            MyCard(taskModel = tasks[task])
        }
    }
}

@Composable
fun MyCard(taskModel: HabitsModel) {
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
                text = taskModel.title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Descripción
            Text(
                text = taskModel.description,
                fontSize = 16.sp,
                color = Color.Gray,
            )
        }
    }
}

@Composable
fun TextInputPopup(show: Boolean, onDismiss: () -> Unit, onTaskAdded: (String, String) -> Unit) {
    var tittle by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    if (show) {
        Dialog(onDismissRequest = { onDismiss() }) {
            Surface(shape = RoundedCornerShape(8.dp), shadowElevation = 8.dp) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Nuevo hábito")

                    TextField(
                        value = tittle,
                        onValueChange = { tittle = it },
                        singleLine = true,
                        maxLines = 1,
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    TextField(
                        value = description,
                        onValueChange = { description = it },
                        singleLine = true,
                        maxLines = 20,
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Column(
                            modifier = Modifier.weight(1f).padding(8.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Button(
                                onClick = {
                                    onTaskAdded(tittle, description)
                                    tittle = ""
                                    description = ""
                                    onDismiss()
                                },
                                modifier = Modifier.padding(end = 8.dp),
                            ) {
                                Text("Guardar")
                            }
                        }
                        Column(
                            modifier = Modifier.weight(1f).padding(8.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Button(onClick = { onDismiss() }) {
                                Text("Cancelar")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview()
@Composable
fun HabitsScreenPreview() {
    HabitsScreen(navController = NavController(LocalContext.current))
}
