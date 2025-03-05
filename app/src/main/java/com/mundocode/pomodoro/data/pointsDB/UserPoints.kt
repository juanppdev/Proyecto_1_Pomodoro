package com.mundocode.pomodoro.data.pointsDB

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_points")
data class UserPoints(@PrimaryKey val userId: String, val points: Int)
