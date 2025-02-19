package com.mundocode.pomodoro.ui.screens.loginScreen

import androidx.activity.ComponentActivity
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.userProfileChangeRequest
import com.mundocode.pomodoro.BuildConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.security.MessageDigest
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val credentialManager: CredentialManager,
) : ViewModel() {

    private val _loginSuccess = MutableStateFlow(false)
    val loginSuccess: StateFlow<Boolean> = _loginSuccess

    init {
        checkUserSession() // Verifica si hay una sesión activa al iniciar
    }

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun registerWithEmail(name: String, email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { authResult ->
                val user = authResult.user
                user?.updateProfile(
                    userProfileChangeRequest {
                        displayName = name // ✅ Guardar el nombre del usuario
                    },
                )?.addOnCompleteListener {
                    if (it.isSuccessful) {
                        _loginSuccess.value = true
                    } else {
                        _errorMessage.value = it.exception?.message
                    }
                }
            }
            .addOnFailureListener { exception ->
                _errorMessage.value = exception.message
            }
    }

    fun loginWithEmail(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                _loginSuccess.value = true
            }
            .addOnFailureListener { exception ->
                _errorMessage.value = exception.message
            }
    }

    private fun checkUserSession() {
        val currentUser = firebaseAuth.currentUser
        _loginSuccess.value = currentUser != null
        if (currentUser != null) {
            Timber.tag("LoginViewModel").d("Usuario autenticado: ${currentUser.email}")
        } else {
            Timber.tag("LoginViewModel").d("No hay sesión activa")
        }
    }

    fun handleGoogleSignIn(activity: ComponentActivity) {
        viewModelScope.launch {
            googleSignIn(activity).collect { result ->
                result.fold(
                    onSuccess = {
                        Timber.tag("LoginViewModel").d("Google sign-in successful")
                        _loginSuccess.value = true
                    },
                    onFailure = { e ->
                        Timber.tag("LoginViewModel").e(e, "Google sign-in failed")
                        _loginSuccess.value = false
                    },
                )
            }
        }
    }

    private fun googleSignIn(activity: ComponentActivity): Flow<Result<AuthResult>> = callbackFlow {
        try {
            val ranNonce: String = UUID.randomUUID().toString()
            val bytes: ByteArray = ranNonce.toByteArray()
            val md: MessageDigest = MessageDigest.getInstance("SHA-256")
            val digest: ByteArray = md.digest(bytes)
            val hashedNonce: String = digest.fold("") { str, it -> str + "%02x".format(it) }

            val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(BuildConfig.WEB_CLIENT_ID)
                .setAutoSelectEnabled(true)
                .setNonce(hashedNonce)
                .build()

            val request: GetCredentialRequest = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            val result = credentialManager.getCredential(activity, request)
            val credential = result.credential

            if (credential is CustomCredential &&
                credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
            ) {
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                val authCredential = GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null)
                val authResult = firebaseAuth.signInWithCredential(authCredential).await()
                trySend(Result.success(authResult))
            } else {
                throw RuntimeException("Received an invalid credential type")
            }
        } catch (_: GetCredentialCancellationException) {
            trySend(Result.failure(Exception("Sign-in was canceled. Please try again.")))
        } catch (e: Exception) {
            trySend(Result.failure(e))
        }
        awaitClose { }
    }
}
