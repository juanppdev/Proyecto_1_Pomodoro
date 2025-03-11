package com.mundocode.pomodoro.core.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mundocode.pomodoro.core.room.PomodoroDatabase.Companion.SESSION_TABLE_NAME
import com.mundocode.pomodoro.model.room.SessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SessionDao {
    @Query("SELECT * FROM $SESSION_TABLE_NAME WHERE date BETWEEN :startDate AND :endDate")
    fun getSessionsBetweenDatesFlow(startDate: String, endDate: String): Flow<List<SessionEntity>>

    @Query("SELECT COUNT(*) FROM $SESSION_TABLE_NAME WHERE type = 'Trabajo' AND date BETWEEN :startDate AND :endDate")
    fun getPomodoroCountBetweenDates(startDate: String, endDate: String): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertSession(session: SessionEntity)
}
