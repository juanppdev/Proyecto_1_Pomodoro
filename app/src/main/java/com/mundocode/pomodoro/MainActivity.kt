package com.mundocode.pomodoro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.mundocode.pomodoro.core.navigation.NavigationRoot
import com.mundocode.pomodoro.ui.theme.PomodoroTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var signInClient: SignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        signInClient = Identity.getSignInClient(this)
        setContent {
            PomodoroTheme {
                NavigationRoot()
            }
        }
    }
}
