package com.mundocode.proyecto_1_pomodoro.ui.screens.timer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.mundocode.proyecto_1_pomodoro.R
import com.mundocode.proyecto_1_pomodoro.ui.viewmodel.TimeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimerScreen(viewModel:TimeViewModel, modifier: Modifier) {
    val timeState = viewModel.timeState.value
    val progress = timeState.remainingTime / (25 * 60 * 1000f)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.app_name))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ){
        paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ){
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(
                    text = if(timeState.isWorking) "Working" else "Resting",
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .background(
                            color = if(timeState.isWorking) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
                            shape = MaterialTheme.shapes.medium
                        )
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    color = Color.White,
                    fontSize = 18.sp
                )
                Box(
                    modifier = Modifier.padding(bottom = 25.dp),
                    contentAlignment = Alignment.Center
                ){
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(200.dp),
                        progress = progress,
                        color = if (timeState.isWorking) Color(0xFFFFC107) else Color(0xFF03A9F4),
                        strokeWidth = 10.dp
                    )
                    Text(
                        text = "${timeState.remainingTime / 1000 / 60}:${(timeState.remainingTime / 1000 % 60).toString().padStart(2, '0')}",
                        textAlign = TextAlign.Center,
                        fontSize = 36.sp,
                        color = Color(0xFF0000FF)
                    )
                }
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(vertical = 16.dp)
            ){
                Button(
                    onClick = { viewModel.startTimer()},
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC107))
                ) {
                    Text(text = "Iniciar", color = Color.White)
                }
                Button(
                    onClick = { viewModel.stopTimer()},
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF03A9F4))
                ) {
                    Text(text = "Detener", color = Color.White)
                }
                Button(
                    onClick = { viewModel.resetTimer()},
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE91E63))
                ) {
                    Icon(Icons.Filled.Refresh, contentDescription = "Reiniciar", tint = Color.White)
                }
            }
            Text(
                text = "Duración Trabajo: ${timeState.workDuration} min",
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }
    }
}
