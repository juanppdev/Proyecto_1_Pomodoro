package com.mundocode.pomodoro.model.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mundocode.pomodoro.core.room.PomodoroDatabase.Companion.TASK_TABLE_NAME

@Entity(tableName = TASK_TABLE_NAME)
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val completed: Boolean = false,
    val category: String = "General", // Categoría de la tarea
)
