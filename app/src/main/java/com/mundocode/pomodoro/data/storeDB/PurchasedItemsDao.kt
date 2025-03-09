package com.mundocode.pomodoro.data.storeDB

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PurchasedItemsDao {
    @Query("SELECT * FROM purchased_items WHERE userId = :userId")
    fun getUserPurchasedItems(userId: String): Flow<List<PurchasedItem>>

    @Query("SELECT * FROM purchased_themes WHERE userId = :userId")
    fun getUserPurchasedThemes(userId: String): Flow<List<PurchasedTheme>>

    @Query("SELECT COUNT(*) FROM purchased_themes WHERE userId = :userId")
    suspend fun countUserPurchasedThemes(userId: String): Int // ✅ Verifica si los temas realmente existen

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPurchasedItem(item: PurchasedItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPurchasedTheme(item: PurchasedTheme)
}
