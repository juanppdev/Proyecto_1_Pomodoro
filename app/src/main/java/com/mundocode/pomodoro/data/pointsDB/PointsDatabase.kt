package com.mundocode.pomodoro.data.pointsDB

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mundocode.pomodoro.data.storeDB.PurchasedItem
import com.mundocode.pomodoro.data.storeDB.PurchasedItemsDao
import com.mundocode.pomodoro.data.storeDB.PurchasedTheme

@Database(entities = [UserPoints::class, PurchasedItem::class, PurchasedTheme::class], version = 2)
abstract class PointsDatabase : RoomDatabase() {
    abstract fun pointsDao(): PointsDao
    abstract fun purchasedItemsDao(): PurchasedItemsDao
}
