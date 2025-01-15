package com.mundocode.proyecto_1_pomodoro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.mundocode.proyecto_1_pomodoro.ui.screens.timer.TimerScreen
import com.mundocode.proyecto_1_pomodoro.ui.theme.Proyecto_1_PomodoroTheme
import com.mundocode.proyecto_1_pomodoro.ui.viewmodel.TimerViewModel

class MainActivity : ComponentActivity() {
    private val timerViewModel = TimerViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Proyecto_1_PomodoroTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
<<<<<<< HEAD
                    TimerScreen(
                       viewModel = timerViewModel,
                        modifier = Modifier.padding(innerPadding)
=======
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding),
>>>>>>> origin/develop
                    )

                }
            }
        }
    }
}
<<<<<<< HEAD
=======

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier,
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Proyecto_1_PomodoroTheme {
        Greeting("Android")
    }
}
>>>>>>> origin/develop
