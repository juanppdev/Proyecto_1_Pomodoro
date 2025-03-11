package com.mundocode.pomodoro.model.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mundocode.pomodoro.core.room.PomodoroDatabase.Companion.USER_POINTS_TABLE_NAME

@Entity(tableName = USER_POINTS_TABLE_NAME)
data class UserPointsEntity(@PrimaryKey val userId: String, val points: Int)
