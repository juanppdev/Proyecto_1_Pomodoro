package com.mundocode.pomodoro.model.local

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.Serializable

@Serializable
data class Timer(val sessionName: String, val mode: String, val timer: String, val pause: String) {
    fun toJson(): String = Json.encodeToString(this)

    companion object {
        fun fromJson(json: String): Timer = Json.decodeFromString(json) // ✅ Función para convertir JSON a Timer
    }
}
