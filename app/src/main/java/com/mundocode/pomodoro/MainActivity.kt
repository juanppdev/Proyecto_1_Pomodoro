package com.mundocode.pomodoro

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.mundocode.pomodoro.core.navigation.NavigationRoot
import com.mundocode.pomodoro.ui.theme.PomodoroTheme
import com.mundocode.pomodoro.ui.theme.ThemeViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var signInClient: SignInClient

    @Inject
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        signInClient = Identity.getSignInClient(this)

        handleNotificationIntent(intent)

        setContent {
            val themeViewModel: ThemeViewModel = hiltViewModel()
            PomodoroTheme(themeViewModel) {
                // ✅ Pasamos el `ThemeViewModel`
                NavigationRoot()
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleNotificationIntent(intent)
    }

    private fun handleNotificationIntent(intent: Intent) {
        intent.extras?.let {
            if (it.getBoolean("fromReminder", false)) {
                // 🚀 Aquí puedes redirigir al usuario al Pomodoro
                Timber.tag("MainActivity").i("Notificación de recordatorio presionada. Redirigiendo al Pomodoro...")
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser

        if (currentUser != null) {
            Timber.tag("MainActivity").i("Usuario autenticado: ${currentUser.email}")
        }
    }
}
