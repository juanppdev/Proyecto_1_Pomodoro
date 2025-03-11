package com.mundocode.pomodoro.core.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mundocode.pomodoro.core.room.PomodoroDatabase.Companion.PURCHASED_THEME_TABLE_NAME
import com.mundocode.pomodoro.model.room.PurchasedThemeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PurchasedThemeDao {

    @Query("SELECT * FROM $PURCHASED_THEME_TABLE_NAME WHERE userId = :userId")
    fun getUserPurchasedThemes(userId: String): Flow<List<PurchasedThemeEntity>>

    @Query("SELECT COUNT(*) FROM $PURCHASED_THEME_TABLE_NAME WHERE userId = :userId")
    suspend fun countUserPurchasedThemes(userId: String): Int // ✅ Verifica si los temas realmente existen

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insert(item: PurchasedThemeEntity)
}
