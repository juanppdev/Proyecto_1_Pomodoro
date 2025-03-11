package com.mundocode.pomodoro.model.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mundocode.pomodoro.core.room.PomodoroDatabase.Companion.PURCHASED_THEME_TABLE_NAME

@Entity(tableName = PURCHASED_THEME_TABLE_NAME)
data class PurchasedThemeEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "userId") val userId: String,
    @ColumnInfo(name = "themeName") val themeName: String,
    @ColumnInfo(name = "themeDescription") val themeDescription: String,
    @ColumnInfo(name = "price") val price: Int,
)
