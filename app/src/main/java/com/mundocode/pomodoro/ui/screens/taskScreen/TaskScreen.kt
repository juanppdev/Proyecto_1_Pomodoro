package com.mundocode.pomodoro.ui.screens.taskScreen

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.mundocode.pomodoro.data.taskDB.TaskEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(navController: NavController, viewModel: TaskViewModel = hiltViewModel()) {
    val tasks by viewModel.tasks.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog = true },
                containerColor = MaterialTheme.colorScheme.primary,
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar tarea")
            }
        },
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(tasks.size) { task ->
                val task = tasks[task]
                TaskItem(
                    task = task,
                    onTaskChecked = { viewModel.toggleTask(it) },
                    onDelete = { viewModel.deleteTask(it) },
                )
            }
        }
    }

    if (showDialog) {
        AddTaskDialog(
            onDismiss = { showDialog = false },
            onTaskAdded = { title, category ->
                viewModel.addTask(title, category)
                showDialog = false
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskItem(task: TaskEntity, onTaskChecked: (TaskEntity) -> Unit, onDelete: (TaskEntity) -> Unit) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            if (it == SwipeToDismissBoxValue.EndToStart) {
                onDelete(task)
                true
            } else {
                false
            }
        },
    )

    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromEndToStart = true, // Habilita deslizamiento de derecha a izquierda
        enableDismissFromStartToEnd = false, // No permite deslizamiento de izquierda a derecha
        backgroundContent = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Red)
                    .padding(16.dp),
                contentAlignment = Alignment.CenterEnd,
            ) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.White)
            }
        },
        content = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Checkbox(
                    checked = task.completed,
                    onCheckedChange = { onTaskChecked(task) },
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = task.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    textDecoration = if (task.completed) TextDecoration.LineThrough else TextDecoration.None,
                )
            }
        },
    )
}

@Composable
fun AddTaskDialog(onDismiss: () -> Unit, onTaskAdded: (String, String) -> Unit) {
    var newTask by rememberSaveable { mutableStateOf("") }
    val categories = listOf("General", "Trabajo", "Estudio", "Personal")
    var selectedCategory by rememberSaveable { mutableStateOf(categories.first()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = {
                    if (newTask.isNotBlank()) {
                        onTaskAdded(newTask.trim(), selectedCategory)
                    }
                },
                enabled = newTask.isNotBlank(),
            ) {
                Text("Agregar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        },
        title = { Text("Agregar nueva tarea") },
        text = {
            Column {
                OutlinedTextField(
                    value = newTask,
                    onValueChange = { newTask = it },
                    label = { Text("Descripción de la tarea") },
                    singleLine = true,
                )

                Spacer(modifier = Modifier.height(8.dp))

                var expanded by remember { mutableStateOf(false) }

                Text("Categoría:")
                Box {
                    OutlinedButton(onClick = { expanded = true }) {
                        Text(selectedCategory)
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                    ) {
                        categories.forEach { category ->
                            DropdownMenuItem(
                                text = { Text(category) },
                                onClick = {
                                    selectedCategory = category
                                    expanded = false
                                },
                            )
                        }
                    }
                }
            }
        },
    )
}

@Composable
@Preview
fun TaskScreenPreview() {
    TaskScreen(navController = NavController(LocalContext.current))
}
