package com.mundocode.pomodoro.data.storeDB

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "purchased_items")
data class PurchasedItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: String,
    val itemName: String,
    val itemDescription: String,
    val price: Int,
)

@Entity(tableName = "purchased_themes")
data class PurchasedTheme(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "userId") val userId: String,
    @ColumnInfo(name = "themeName") val themeName: String,
    @ColumnInfo(name = "themeDescription") val themeDescription: String,
    @ColumnInfo(name = "price") val price: Int,
)
