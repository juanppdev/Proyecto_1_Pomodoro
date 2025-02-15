package com.mundocode.pomodoro.ui.screens.habits

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.mundocode.pomodoro.R
import com.mundocode.pomodoro.ui.components.CustomTopAppBar
import com.mundocode.pomodoro.ui.components.DialogPopUp
import com.mundocode.pomodoro.ui.components.SwipeBox
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

    var title by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }

    MaterialTheme {
        Scaffold(
            topBar = {
                CustomTopAppBar(
                    navController = navController,
                    title = "Mis Hábitos",
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
            HabitsContent(
                uiState = uiState,
                showDialog = showDialog,
                innerPadding = innerPadding,
                title = title,
                description = description,
            )
        }
    }
}

@Composable
fun HabitsContent(
    viewModel: HabitsViewModel = hiltViewModel(),
    uiState: HabitsUIState,
    showDialog: Boolean,
    innerPadding: PaddingValues,
    title: String,
    description: String,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
    ) {
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
                val error = uiState
                Text(text = error.throwable.message ?: "Error desconocido")
            }

            HabitsUIState.Loading -> {
                CircularProgressIndicator()
            }

            is HabitsUIState.Success -> {
                DialogPopUp(
                    showDialog,
                    onDismiss = { viewModel.onDialogClose() },
                    onTaskAdded = { tittle, description -> viewModel.onTaskCreated(tittle, description) },
                    title = title,
                    description = description,
                    onValueChangeTitle = { it },
                    onValueChangeDescription = { it },
                )

                TasksList(
                    uiState.tasks,
                )
            }
        }
    }
}

@Preview
@Composable
fun HabitsContentPreview() {
    HabitsContent(
        uiState = HabitsUIState.Success(
            listOf(
                HabitsModel(1, "Habito 1", "Descripcion 1"),
                HabitsModel(2, "Habito 2", "Descripcion 2"),
            ),
        ),
        showDialog = false,
        innerPadding = PaddingValues(16.dp),
        title = "",
        description = "",
    )
}

@Composable
fun TasksList(tasks: List<HabitsModel>) {
    LazyColumn {
        items(tasks.size) { task ->
            MyCard(taskModel = tasks[task], habitsViewModel = viewModel())
        }
    }
}

@Composable
fun MyCard(taskModel: HabitsModel, habitsViewModel: HabitsViewModel) {
    SwipeBox(
        onDelete = {
            habitsViewModel.onItemRemove(taskModel)
        },
        modifier = Modifier,
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
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
}

@Preview()
@Composable
fun HabitsScreenPreview() {
    HabitsScreen(navController = NavController(LocalContext.current))
}
