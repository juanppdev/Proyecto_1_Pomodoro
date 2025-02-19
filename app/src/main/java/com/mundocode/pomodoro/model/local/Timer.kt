package com.mundocode.pomodoro.model.local

import kotlinx.serialization.Serializable

@Serializable
data class Timer(var sessionName: String, val mode: String, val timer: String, val pause: String)
