package com.mundocode.pomodoro.data.taskDB

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val completed: Boolean = false,
    val category: String = "General", // Categoría de la tarea
)
