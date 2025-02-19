package com.mundocode.pomodoro.data.sessionDb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SessionDao {
    @Insert
    suspend fun insertSession(session: SessionEntity)

    @Query("SELECT * FROM sessions WHERE date BETWEEN :startDate AND :endDate")
    suspend fun getSessionsBetweenDates(startDate: String, endDate: String): List<SessionEntity>
}
