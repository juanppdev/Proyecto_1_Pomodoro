package com.mundocode.pomodoro.di

import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth

private lateinit var auth: FirebaseAuth

fun authFirebase() {
    fun onStart() {
        val currentUser = auth.currentUser
    }
}
