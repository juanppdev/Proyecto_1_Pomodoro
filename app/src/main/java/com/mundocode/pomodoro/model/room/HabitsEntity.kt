package com.mundocode.pomodoro.model.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mundocode.pomodoro.core.room.PomodoroDatabase.Companion.HABITS_TABLE_NAME

@Entity(tableName = HABITS_TABLE_NAME)
data class HabitsEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0, // ✅ Auto-generar IDs únicos en Room
    val title: String,
    val description: String,
)
