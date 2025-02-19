package com.mundocode.pomodoro.data.habitsDB

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class HabitsEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // ✅ Auto-generar IDs únicos en Room
    val title: String,
    val description: String,
)
