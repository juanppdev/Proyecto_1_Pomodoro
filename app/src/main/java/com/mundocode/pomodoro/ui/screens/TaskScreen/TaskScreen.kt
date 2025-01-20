@file:OptIn(ExperimentalMaterial3Api::class)

package com.mundocode.pomodoro.ui.screens.tasks

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mundocode.pomodoro.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(navController: NavController) {
    val context = LocalContext.current

    var tasks by remember { mutableStateOf(listOf("Modo actual", "Modo actual 2")) }
    var newTask by remember { mutableStateOf("") }

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
                            contentDescription = "Localized description"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Lista de tareas",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            tasks.forEachIndexed { index, task ->
                TaskItem(
                    task = task,
                    onTaskChecked = { isChecked ->
                        if (isChecked) {
                            tasks = tasks.toMutableList().apply { removeAt(index) }
                            Toast.makeText(context, "Tarea completada", Toast.LENGTH_SHORT).show()
                        }
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BasicTextField(
                    value = newTask,
                    onValueChange = { newTask = it },
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp)
                        .background(Color.White, shape = MaterialTheme.shapes.small)
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    keyboardActions = KeyboardActions(onDone = {
                        if (newTask.isNotBlank()) {
                            tasks = tasks + newTask
                            newTask = ""
                        }
                    })
                )
                IconButton(
                    onClick = {
                        if (newTask.isNotBlank()) {
                            tasks = tasks + newTask
                            newTask = ""
                        }
                    },
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Add,
                        contentDescription = "Añadir tarea",
                        tint = Color(0xFF4CAF50)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    Toast.makeText(context, "TODO: Implementar vista de todas las tareas", Toast.LENGTH_SHORT).show()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6366F1))
            ) {
                Text(
                    text = "Ver todas las tareas",
                    fontSize = 18.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun TaskItem(task: String, onTaskChecked: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(8.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = false,
            onCheckedChange = { onTaskChecked(it) },
            colors = CheckboxDefaults.colors(checkedColor = Color(0xFF4CAF50))
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = task,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
@Preview
fun TaskScreenPreview() {
    TaskScreen(navController = NavController(LocalContext.current))
}
