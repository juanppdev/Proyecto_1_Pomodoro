package com.mundocode.pomodoro.model

import com.mundocode.pomodoro.model.local.Habits
import com.mundocode.pomodoro.model.room.HabitsEntity

fun Habits.toEntity(): HabitsEntity = HabitsEntity(title = this.title, description = this.description)
fun HabitsEntity.toDto(): Habits = Habits(this.id, this.title, this.description)
