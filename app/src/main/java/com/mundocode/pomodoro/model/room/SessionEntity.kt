package com.mundocode.pomodoro.model.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mundocode.pomodoro.core.room.PomodoroDatabase.Companion.SESSION_TABLE_NAME

@Entity(tableName = SESSION_TABLE_NAME)
data class SessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // Este es opcional y debe ser el único Int
    val type: String, // "Trabajo" o "Descanso"
    val duration: String, // Duración en formato mm:ss
    val date: String, // Fecha en formato yyyy-MM-dd HH:mm:ss // FIXME: change to Date
)
