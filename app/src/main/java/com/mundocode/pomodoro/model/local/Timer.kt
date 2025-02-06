package com.mundocode.pomodoro.model.local

import kotlinx.serialization.Serializable

@Serializable
data class Timer(var sessionName: String, val mode: String, val timer: String, val pause: String) {
    constructor() : this(
        sessionName = "",
        mode = "",
        timer = "30:00",
        pause = "5:00",
    )
}
