package com.mundocode.pomodoro.data.habitsDB

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitsDao {

    @Query("SELECT * FROM HabitsEntity")
    fun getHabits(): Flow<List<HabitsEntity>> // ✅ Asegurar que devuelve un Flow

    @Query("SELECT * FROM HabitsEntity WHERE id = :id LIMIT 1")
    suspend fun getHabitById(id: Int): HabitsEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE) // ✅ Evita duplicados reemplazando registros existentes
    suspend fun addHabit(habit: HabitsEntity)

    @Update
    suspend fun updateHabit(habit: HabitsEntity)

    @Delete
    suspend fun deleteHabit(habit: HabitsEntity)

    @Query("DELETE FROM HabitsEntity")
    suspend fun clearHabits()
}
