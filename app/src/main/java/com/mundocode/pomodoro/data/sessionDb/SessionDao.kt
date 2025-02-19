package com.mundocode.pomodoro.data.sessionDb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SessionDao {
    @Query("SELECT * FROM sessions WHERE date BETWEEN :startDate AND :endDate")
    fun getSessionsBetweenDatesFlow(startDate: String, endDate: String): Flow<List<SessionEntity>> // ✅ Devuelve Flow

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: SessionEntity)
}
