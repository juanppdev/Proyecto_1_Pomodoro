package com.mundocode.pomodoro.ui.screens.taskScreen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mundocode.pomodoro.R
import com.mundocode.pomodoro.ui.components.CustomTopAppBar

@Composable
fun TaskItem(task: String, isCompleted: Boolean = false, onTaskChecked: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                if (isCompleted) Color.LightGray else Color.White,
                shape = RoundedCornerShape(8.dp),
            )
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(
            checked = isCompleted,
            onCheckedChange = { onTaskChecked(it) },
            colors = CheckboxDefaults.colors(
                checkedColor = Color(0xFFD32F2F),
                uncheckedColor = Color(0xFF000000),
                checkmarkColor = Color.White,
            ),
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = task,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            color = if (isCompleted) Color(0xFF1E1E1E) else Color.Black,
            textDecoration = if (isCompleted) TextDecoration.LineThrough else TextDecoration.None,
            modifier = Modifier.weight(1f),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(navController: NavController) {
    val context = LocalContext.current

    var tasks by remember { mutableStateOf(listOf("Modo actual", "Modo actual 2")) }
    var completedTasks by remember { mutableStateOf(listOf<String>()) }

    Scaffold(
        topBar = {
            CustomTopAppBar(
                navController = navController,
                title = stringResource(id = R.string.app_name),
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
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
        ) {
            // not finished tasks
            Text(
                text = "Tareas no finalizadas",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp),
            )

            tasks.forEachIndexed { index, task ->
                TaskItem(
                    task = task,
                    isCompleted = false,
                    onTaskChecked = { isChecked ->
                        if (isChecked) {
                            tasks = tasks.toMutableList().apply { removeAt(index) }
                            completedTasks = completedTasks + task
                            Toast.makeText(context, "Tarea completada", Toast.LENGTH_SHORT).show()
                        }
                    },
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Tareas realizadas
            if (completedTasks.isNotEmpty()) {
                Text(
                    text = "Tareas realizadas",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 16.dp),
                )

                completedTasks.forEachIndexed { index, task ->
                    TaskItem(
                        task = task,
                        isCompleted = true,
                        onTaskChecked = { isChecked ->
                            if (!isChecked) {
                                completedTasks = completedTasks.toMutableList().apply { removeAt(index) }
                                tasks = tasks + task
                                Toast.makeText(context, "Tarea movida a no finalizadas", Toast.LENGTH_SHORT).show()
                            }
                        },
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
@Preview
fun TaskScreenPreview() {
    TaskScreen(navController = NavController(LocalContext.current))
}
