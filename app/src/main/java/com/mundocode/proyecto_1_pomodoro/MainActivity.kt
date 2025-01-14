package com.mundocode.proyecto_1_pomodoro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mundocode.proyecto_1_pomodoro.ui.screens.timer.TimerScreen
import com.mundocode.proyecto_1_pomodoro.ui.theme.Proyecto_1_PomodoroTheme
import com.mundocode.proyecto_1_pomodoro.ui.viewmodel.TimeViewModel

class MainActivity : ComponentActivity() {
    private val timeViewModel = TimeViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Proyecto_1_PomodoroTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TimerScreen(
                       viewModel = timeViewModel,
                        modifier = Modifier.padding(innerPadding)
                    )

                }
            }
        }
    }
}
