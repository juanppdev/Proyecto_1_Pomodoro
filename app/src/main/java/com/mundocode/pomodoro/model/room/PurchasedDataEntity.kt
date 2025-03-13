package com.mundocode.pomodoro.model.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mundocode.pomodoro.core.room.PomodoroDatabase.Companion.PURCHASED_DATA_TABLE_NAME

@Entity(tableName = PURCHASED_DATA_TABLE_NAME)
data class PurchasedDataEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: String,
    val itemName: String,
    val itemDescription: String,
    val price: Int,
)
