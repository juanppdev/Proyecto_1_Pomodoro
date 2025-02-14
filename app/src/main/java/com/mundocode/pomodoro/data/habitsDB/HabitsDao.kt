package com.mundocode.pomodoro.data.habitsDB

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitsDao {

    @Query("SELECT * FROM HabitsEntity")
    fun getHabits(): Flow<List<HabitsEntity>>

    @Insert
    suspend fun addHabit(habit: HabitsEntity)

    @Update
    suspend fun updateHabit(habit: HabitsEntity)

    @Delete
    suspend fun deleteHabit(habit: HabitsEntity)
}
