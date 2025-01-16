package com.mundocode.pomodoro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.mundocode.pomodoro.core.navigation.NavigationRoot
import com.mundocode.pomodoro.ui.theme.PomodoroTheme
import com.mundocode.pomodoro.ui.viewmodel.TimerViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val timerViewModel = TimerViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            PomodoroTheme {
                NavigationRoot()
            }
        }
    }
}
