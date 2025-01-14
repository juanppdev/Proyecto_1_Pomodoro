package com.mundocode.proyecto_1_pomodoro.ui.screens.timer

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
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
                .padding(paddingValues)
        ){
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                CircularProgressIndicator(
                    modifier = Modifier
                        .width(256.dp)
                        .height(256.dp),
                    progress = progress,
                )
                Text(
                    text = "${timeState.remainingTime / 1000}s",
                    modifier = Modifier.padding(bottom = 8.dp),
                    textAlign = TextAlign.Center
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ){
                IconButton(
                    modifier = Modifier.padding(bottom = 8.dp),
                    onClick = {
                        viewModel.startTimer()
                    }
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_launcher_background),
                        contentDescription = "Start"
                    )
                }
                IconButton(
                    modifier = Modifier.padding(bottom = 8.dp),
                    onClick = { viewModel.resetTimer() }
                ) {
                    Icon(imageVector = Icons.Filled.Refresh, contentDescription = "Reset")
                }
            }
        }
    }
}
