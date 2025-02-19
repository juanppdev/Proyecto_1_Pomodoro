package com.mundocode.pomodoro.model.local

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString

@Serializable
data class Timer(
    val sessionName: String = "",
    val mode: String = "",
    val timer: String = "30:00",
    val pause: String = "05:00",
) {
    fun toJson(): String = Json.encodeToString(this)

    companion object {
        fun fromJson(json: String): Timer = Json.decodeFromString(json)
    }
}
