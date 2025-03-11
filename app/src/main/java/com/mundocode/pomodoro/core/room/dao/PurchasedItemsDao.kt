package com.mundocode.pomodoro.core.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mundocode.pomodoro.core.room.PomodoroDatabase.Companion.PURCHASED_ITEMS_TABLE_NAME
import com.mundocode.pomodoro.model.room.PurchasedItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PurchasedItemsDao {
    @Query("SELECT * FROM $PURCHASED_ITEMS_TABLE_NAME WHERE userId = :userId")
    fun get(userId: String): Flow<List<PurchasedItemEntity>>

    @Query("SELECT COUNT(*) FROM $PURCHASED_ITEMS_TABLE_NAME WHERE userId = :userId")
    suspend fun countUserPurchasedItem(userId: String): Int // ✅ Verifica si los temas realmente existen

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insert(item: PurchasedItemEntity)
}
